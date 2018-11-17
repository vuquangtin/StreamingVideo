package com.org.videomonitor.tables;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

public class UrlTableModel extends DefaultTableModel {
	static Logger logger = Logger.getLogger(UrlTableModel.class.getName());
	private static final long serialVersionUID = 193034139870407944L;
	List<String> listUrl=new ArrayList<>();
	// Vector data;
	// Vector cols;

	static String[] columns = new String[] { "STT", "Url" };

	public UrlTableModel() {
		super(columns, 0);

	}

	@Override
	public void setValueAt(Object aValue, int row, int column) {

	}
	@Override
	public int getRowCount() {
		if (listUrl != null) {
			try {
				return listUrl.size();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return 0;

	}
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return rowIndex + 1;
		case 1:
			return listUrl.get(rowIndex);
		}
		return null;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		Class<?> clazz = String.class;
		switch (columnIndex) {
		case 0:
			clazz = String.class;
			break;
		case 1:
			clazz = String.class;
			break;		

		}
		return clazz;
	}

	public void addRow(String url, boolean isRefesh) {
		if (logger.isDebugEnabled()) {
			logger.debug("addRow:" + url);
		}

		this.listUrl.add(url);

		if (isRefesh) {
			refeshData();
			this.fireTableDataChanged();
		}

	}

	public void updateListRow(List<String> listUrl) {
		if (listUrl != null && listUrl.size() > 0) {

			this.listUrl.clear();
			if (logger.isDebugEnabled()) {
				logger.debug("this.listProfiles.clear:" + this.listUrl.size());
			}
			for (String item : listUrl) {
				if (item != null) {
					addRow(item, false);
				}
			}
			refeshData();
			this.fireTableDataChanged();
		} else {
			refeshData();
			this.fireTableDataChanged();
		}
	}

	private void refeshData() {
		if (logger.isDebugEnabled()) {
			logger.debug("TOTAL:" + this.listUrl.size());
		}
		this.fireTableDataChanged();
	}
}