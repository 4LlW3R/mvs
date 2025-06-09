package com.epam.tcodata.analytics.overtaking.violation.detection.policies.area;

public interface IGeoJsonReadable<T> {

    T fromJson(String s);
}
