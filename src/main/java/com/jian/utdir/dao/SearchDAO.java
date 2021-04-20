package com.jian.utdir.dao;

import java.util.List;

import com.jian.utdir.dto.DocDTO;

public interface SearchDAO {

	List<List<DocDTO>> search(List<String> terms);
}
