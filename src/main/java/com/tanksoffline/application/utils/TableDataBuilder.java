package com.tanksoffline.application.utils;

import com.tanksoffline.core.utils.Builder;
import com.tanksoffline.core.utils.Factory;
import com.tanksoffline.core.utils.SingletonFactory;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class TableDataBuilder<T> implements Builder<ObservableList<T>> {
    private Factory<ObservableList<T>> resultListFactory;
    private ObjectProperty<Predicate<T>> targetPredicateProperty;
    private ObservableMap<Property<?>, Predicate<T>> predicateMap;
    private Callable<List<T>> builtData;
    private ReadOnlyProperty<Comparator<T>> comparatorProperty;

    public TableDataBuilder() {
        targetPredicateProperty = new SimpleObjectProperty<>(t -> true);
        predicateMap = FXCollections.observableHashMap();
        predicateMap.addListener((MapChangeListener<Property<?>, Predicate<T>>) change -> {
            Predicate<T> init = t -> true;
            for (Predicate<T> p : predicateMap.values()) {
                init = init.and(p);
            }
            targetPredicateProperty.setValue(init);
        });

        resultListFactory = new SingletonFactory<>(() -> {
            if (builtData == null)
                throw new RuntimeException("Set source for built data");
            try {
                return FXCollections.observableList(builtData.call());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public TableDataBuilder<T> setBuiltData(Callable<List<T>> builtData) {
        this.builtData = builtData;
        return this;
    }

    public TableDataBuilder<T> setSorted(ReadOnlyObjectProperty<Comparator<T>> comparatorProperty) {
        this.comparatorProperty = comparatorProperty;
        return this;
    }

    public <E> TableDataBuilder<T> setFilter(Property<E> property, BiPredicate<T, E> predicate) {
        property.addListener((observable, oldValue, newValue) -> {
            predicateMap.put(property, t -> predicate.test(t, newValue));
        });
        predicateMap.put(property, t -> predicate.test(t, property.getValue()));
        return this;
    }

    public ObservableList<T> getBuiltData() {
        return resultListFactory.create();
    }

    @Override
    public ObservableList<T> build() {
        FilteredList<T> filteredList = new FilteredList<>(getBuiltData());
        if (!predicateMap.isEmpty()) {
            filteredList.predicateProperty().bind(targetPredicateProperty);
        }

        SortedList<T> sortedList = new SortedList<>(filteredList);
        if (comparatorProperty != null) {
            sortedList.comparatorProperty().bind(comparatorProperty);
        }

        return sortedList;
    }
}
