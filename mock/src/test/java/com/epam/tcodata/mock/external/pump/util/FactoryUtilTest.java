package com.epam.tcodata.mock.external.pump.util;

import com.epam.tcodata.common.FactoryUtil;
import com.epam.tcodata.common.exception.WrongFactoryClassException;
import com.epam.tcodata.external.pump.factory.IExternalFactory;
import com.epam.tcodata.external.pump.factory.impl.ExternalPositionFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static junit.framework.TestCase.assertTrue;

public class FactoryUtilTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(FactoryUtilTest.class);

    @Test
    public void loadFactoryTest() throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        IExternalFactory positionFactory = FactoryUtil.loadFactory(IExternalFactory.class,
                "com.epam.tcodata.external.pump.factory.impl.ExternalPositionFactory");
        assertTrue(positionFactory instanceof ExternalPositionFactory);
    }

    @Test(expected = ClassNotFoundException.class)
    public void loadAbsentFactoryTest()
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        LOGGER.info("Testing load absent factory..");
        FactoryUtil.loadFactory(IExternalFactory.class, "com.epam.tcodata.external.pump.PositionFactory");
    }

    @Test(expected = WrongFactoryClassException.class)
    public void loadFactoryThatIsNotDerivedFromBaseFactoryTest()
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        FactoryUtil.loadFactory(IExternalFactory.class, "com.epam.tcodata.external.pump.converter.impl.AssetConverter");
    }
}
