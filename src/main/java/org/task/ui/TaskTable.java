package org.task.ui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.MatchMode;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.task.Task;
import org.task.db.DbTask;

/**
 * Implementation of LazyDataModel render Tasks as Table.
 */
public class TaskTable extends LazyDataModel<Task> {

	private static final long serialVersionUID = 1L;
	private final DataSource ds;
	private String folderId;

	public TaskTable(DataSource dataSource, String folderId) {
		this.ds = dataSource;
		this.folderId = folderId;
	}

	// private Function<T, String> onRowKey;
	// private BiFunction<String, TaskInTable<T>, T> onRowData;

	@Override
	public String getRowKey(Task row) {
		return row.id();
	}

	@Override
	public Task getRowData(String rowKey) {
		List<Task> wrappedData = getWrappedData();
		if (wrappedData != null) {
			for (Task task : wrappedData) {
				if (task.ident(rowKey)) { return task; }
			}
		}
		return null;
	}

	@Override
	public int count(Map<String, FilterMeta> filterBy) {
		if (folderId != null && filterBy != null && !filterBy.containsKey("folderid")) {
			filterBy.put(
					"folderid",
					FilterMeta.builder().field("folderid").filterValue(folderId).matchMode(MatchMode.EQUALS).build()
			);
		}
		Params params = new Params();
		String countQuery = countQuery(filterBy, params);
		try (Connection connection = ds.getConnection();
				PreparedStatement stmt = connection.prepareStatement(countQuery)) {
			// set parameters: filter
			params.prepare(stmt);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) { return rs.getInt(1); }
			}
		} catch (Exception e) {
			throw new RuntimeException("Fehler beim Zählen der Tasks: " + e.getMessage(), e);
		}
		return 0;
	}

	@Override
	public List<Task> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
		if (folderId != null && filterBy != null && !filterBy.containsKey("folderid")) {
			filterBy.put(
					"folderid",
					FilterMeta.builder().field("folderid").filterValue(folderId).matchMode(MatchMode.EQUALS).build()
			);
		}
		sortBy.put("description", SortMeta.builder().field("description").order(SortOrder.ASCENDING).build());
		Params params = new Params();
		String pageQuery = pageQuery(first, pageSize, sortBy, filterBy, params);
		List<Task> tasks = new ArrayList<>(0);
		try (Connection connection = ds.getConnection();
				PreparedStatement stmt = connection.prepareStatement(pageQuery)) {
			// set all parameters: filter and paging
			params.prepare(stmt);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					tasks.add(new DbTask(ds, rs));
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Fehler beim Laden der Tasks: " + e.getMessage(), e);
		}
		return tasks;
	}

	// COUNT-Query with Filters
	private String countQuery(Map<String, FilterMeta> filterBy, Params params) {
		return "SELECT COUNT(*) FROM task" + toWhere(filterBy, params);
	}

	// Daten-Query with ordering, Filters and Paging
	// fill parameters of filter and paging
	private String pageQuery(
			int offset, int limit, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy, Params params
	) {
		return "SELECT * FROM task" +
				toWhere(filterBy, params) +
				toOrderBy(sortBy) +
				toOffset(offset, params) +
				toLimit(limit, params);
	}

	private String toLimit(int limit, Params params) {
		if (limit > 0) {
			params.add(limit);
			return " LIMIT ?"; // or "FETCH FIRST ? ROWS ONLY"
		}
		return "";
	}

	private String toOffset(int offset, Params params) {
		if (offset > 0) {
			params.add(offset);
			return " OFFSET ?"; // or "OFFSET ? ROWS"
		}
		return "";
	}

	private String toOrderBy(Map<String, SortMeta> sortBy) {
		String orderBy = sortBy.entrySet().stream()
				.filter(e -> e.getValue().getOrder() != SortOrder.UNSORTED && e.getKey() != null).map(e -> {
					return e.getKey() + (e.getValue().getOrder() == SortOrder.ASCENDING ? " ASC" : " DESC");
				}).collect(Collectors.joining(", "));
		if (!"".equals(orderBy)) {
			orderBy = "  ORDER BY " + orderBy;
		}
		return orderBy;
	}

	public String toWhere(Map<String, FilterMeta> filterBy, Params parameters) {
		String filter = "";
		String wildCard = "%";
		if (filterBy == null || filterBy.isEmpty()) { return filter; }
		for (Map.Entry<String, FilterMeta> entry : filterBy.entrySet()) {
			FilterMeta filterMeta = entry.getValue();
			String column = filterMeta.getField();
			Object value = filterMeta.getFilterValue();
			MatchMode mode = filterMeta.getMatchMode();
			if (value == null || value.toString().trim().isEmpty()) {
				continue;
			}
			if (mode == MatchMode.CONTAINS) {
				// %value%
				String val = wildCard + value.toString().toLowerCase() + wildCard;
				filter = filter + "AND " + column + " LIKE LOWER(?)";
				parameters.add(val);
			} else if (mode == MatchMode.STARTS_WITH) {
				// value%
				String val = value.toString().toLowerCase() + wildCard;
				filter = filter + "AND " + column + " LIKE LOWER(?)";
				parameters.add(val);
			} else if (mode == MatchMode.ENDS_WITH) {
				// %value
				String val = wildCard + value.toString().toLowerCase();
				filter = filter + "AND " + column + " LIKE LOWER(?)";
				parameters.add(val);
			} else if (mode == MatchMode.EQUALS) {
				// value
				if (value instanceof String) {
					String val = value.toString().toLowerCase();
					filter = filter + "AND " + column + " = LOWER(?)";
					parameters.add(val);
				} else if (value instanceof Number) {
					String val = value.toString();
					filter = filter + "AND " + column + " = ?";
					parameters.add(val);
				} else if (value instanceof Date) {
					Timestamp val = new Timestamp(((Date) value).getTime());
					filter = filter + "AND " + column + " = ?";
					parameters.add(val);
				} else if (value instanceof LocalDate) {
					Timestamp val = Timestamp.valueOf(((LocalDate) value).atStartOfDay());
					filter = filter + "AND " + column + " = ?";
					parameters.add(val);
				}
			}
			if (mode == MatchMode.BETWEEN && (value instanceof List)) {
				@SuppressWarnings("unchecked")
				List<LocalDate> dates = (List<LocalDate>) value;
				if (dates.size() == 2) {
					LocalDateTime minStartOfDay = dates.get(0).atStartOfDay();
					LocalDateTime maxStartOfDay = dates.get(1).atStartOfDay();
					Timestamp minDate = Timestamp.valueOf(minStartOfDay);
					Timestamp maxDate = Timestamp.valueOf(maxStartOfDay.plusHours(24));
					filter = filter + "AND " + column + " >= ? AND " + column + " <= ?";
					parameters.add(minDate);
					parameters.add(maxDate);
				}
			}
		}
		if (!"".equals(filter)) {
			filter = "  WHERE " + filter.replaceFirst("AND ", "");
		}
		return filter;
	}

	// collection for query parameters
	static class Params {

		private Map<Integer, Object> entries = new HashMap<>(0);

		public Params() {
			this(new HashMap<>(0));
		}

		public void prepare(PreparedStatement stmt) throws SQLException {
			for (Entry<Integer, Object> e : entries.entrySet()) {
				stmt.setObject(e.getKey(), e.getValue());
			}
		}

		public Params(Map<Integer, Object> entries) {
			this.entries.putAll(entries);
		}

		public Params(Object... params) {
			add(params);
		}

		public void clear() {
			entries.clear();
		}

		public void add(int index, Object value) {
			entries.put(entries.size() + 1, value);
		}

		public void add(Object... params) {
			for (Object param : params) {
				entries.put(entries.size() + 1, param);
			}
		}

		public Object[] values() {
			Object[] values = new Object[entries.size()];
			for (Entry<Integer, Object> e : entries.entrySet()) {
				values[e.getKey() - 1] = e.getValue();
			}
			return values;
		}
	}
}