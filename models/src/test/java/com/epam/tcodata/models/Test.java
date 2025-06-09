package com.epam.tcodata.models;

import com.epam.tcodata.models.datalake.raw.fact.RawPosition;

import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.assertArrayEquals;

public class Test {

    @org.junit.Test
    public void shouldReturnOrderedValues() {

        for (int i = 0; i < 20; i++) {
            System.out.println(UUID.randomUUID());
        }
    }

}
