package com.utfinancing.financehub.etl.financial.util;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ShowBoxUtil {
	
	public static String getText() {
		// 解析SQL
		JTextArea textArea = new JTextArea(50, 40);
		textArea.setLineWrap(true); // 自动换行
		textArea.setWrapStyleWord(true); // 按词换行
		// 使用 JScrollPane 包装 JTextArea
		JScrollPane scrollPane = new JScrollPane(textArea);
		// 创建输入对话框
		int option = JOptionPane.showConfirmDialog(null, scrollPane, "输入原报文", JOptionPane.OK_CANCEL_OPTION);
		return textArea.getText();
	}

}
