package com.biit.drools.plugin;

import com.biit.drools.plugin.configuration.LiferayPluginConfigurationReader;
import com.biit.liferay.model.IArticle;
import com.biit.utils.pool.SimplePool;

public class LiferayArticlePluginPool extends SimplePool<Long, IArticle<Long>> {

	@Override
	public boolean isDirty(IArticle<Long> element) {
		return false;
	}

	@Override
	public long getExpirationTime() {
		return LiferayPluginConfigurationReader.getInstance().getArticlePoolExpirationTime();
	}

}
