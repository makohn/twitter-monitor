package de.htwsaar.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.htwsaar.db.KeywordDao;
import de.htwsaar.model.Keyword;

@Service
public class KeywordService {

	private KeywordDao keywordDao;

	@Autowired
	public KeywordService(KeywordDao keywordDao) {
		this.keywordDao = keywordDao;
	}

	public void insertKeyword(Keyword keyword) {
		keywordDao.insertKeyword(keyword);
	}

	public void deleteKeyword(Keyword keyword) {
		keywordDao.deleteKeyword(keyword);
	}

	public List<Keyword> getKeywords(String username, boolean positive) {
		return keywordDao.getKeywords(username, positive);
	}

	public void switchActive(Keyword keyword) {
		keyword.setActive(!keyword.getActive());
		keywordDao.insertKeyword(keyword);
	}
}