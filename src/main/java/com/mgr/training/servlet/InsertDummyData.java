package com.mgr.training.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Throwables;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mgr.training.data.SeedDummyData;

@Singleton
public class InsertDummyData extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private SeedDummyData dummyData;

	@Inject
	public InsertDummyData(SeedDummyData dummyData) {
		this.dummyData = dummyData;
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	};

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			dummyData.seedData();
		} catch (Exception e) {
			Throwables.propagate(e);
		}
		resp.getWriter().write("<h1>Data bootsratp is done...</h1>");
	}
}