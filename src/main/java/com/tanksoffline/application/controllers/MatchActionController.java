package com.tanksoffline.application.controllers;

import com.tanksoffline.application.data.Match;
import com.tanksoffline.application.data.User;
import com.tanksoffline.application.entities.MatchEntity;
import com.tanksoffline.application.entities.UserEntity;
import com.tanksoffline.application.models.MatchActiveModel;
import com.tanksoffline.application.models.UserActiveModel;
import com.tanksoffline.core.data.Search;
import com.tanksoffline.core.mvc.ActionController;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class MatchActionController implements ActionController<Match> {
    private MatchActiveModel matchActiveModel;
    private Search<MatchEntity> matchSearch;

    @Override
    public Callable<Match> create(Map<String, Object> values) {
        return () -> {
            if (matchActiveModel == null) {
                matchActiveModel = new MatchActiveModel((UserEntity) values.get("user"), (Match.Result) values.get("result"));
            }
            return matchActiveModel.save();
        };
    }

    @Override
    public Callable<? extends Match> findBy(Map<String, Object> values) {
        return () -> {
            List<? extends Match> matches = matchSearch.findBy(values);
            return (matches.size() == 0) ? null : matches.get(0);
        };
    }

    @Override
    public Callable<Match> update(Map<String, Object> values) {
        return () -> matchActiveModel.update();
    }

    @Override
    public Callable<Match> remove() {
        return () -> matchActiveModel.remove();
    }

    @Override
    public Callable<List<? extends Match>> list() {
        return () -> matchSearch.findAll();
    }

    @Override
    public Callable<Match> construct(Match match) {
        return () -> matchActiveModel = new MatchActiveModel(match);
    }

    @Override
    public Callable<Match> destroy() {
        return () -> {
            matchActiveModel = null;
            matchSearch = null;
            return null;
        };
    }
}
