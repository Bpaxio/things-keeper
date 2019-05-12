package ru.bbpax.keeper.filter.core;

import java.util.List;

public interface CompositeFilter extends Filter {

    List<Filter> filters();
}
