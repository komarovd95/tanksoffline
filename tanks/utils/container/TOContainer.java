package tanks.utils.container;

import tanks.services.Service;

import java.util.ArrayList;
import java.util.List;

public class TOContainer implements Container {
    private List<Container> containers;

    public TOContainer() {
        containers = new ArrayList<>();
    }

    @Override
    public void configure() {

    }
    // ServiceLocator



}
