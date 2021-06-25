package com.revature.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public interface GenericRepo<T> {
	// Create
	public T add(T t);
	
	// Read
	public T getById(Integer id);
	public Map<Integer, T> getAll();
	
	// Update
	public boolean update(T t);
	
	// Delete
	public boolean delete(T t);
	
	public T make(ResultSet rs) throws SQLException;
}
