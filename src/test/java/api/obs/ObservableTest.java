package api.obs;

import com.tanksoffline.core.utils.observer.Observable;
import com.tanksoffline.core.utils.observer.Observer;
import com.tanksoffline.core.utils.observer.SimpleProperty;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
public class ObservableTest {
    private Observable<String> observableString;
    private Observer<String> observer;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() throws Exception {
        observableString = new SimpleProperty<>();

        observer = (Observer<String>) PowerMockito.mock(Observer.class);
        observableString.addObserver(observer);
    }

    @Test
    public void testGet() throws Exception {
        assertNull(observableString.get());
    }

    @Test
    public void testSet() throws Exception {
        observableString.set("Hello");
        assertEquals("Hello", observableString.get());
        Mockito.verify(observer).observe(observableString, null, "Hello");
    }
}