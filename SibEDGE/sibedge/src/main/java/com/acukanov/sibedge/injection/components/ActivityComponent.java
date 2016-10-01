package com.acukanov.sibedge.injection.components;


import com.acukanov.sibedge.injection.annotations.PerActivity;
import com.acukanov.sibedge.injection.modules.ActivityModule;
import com.acukanov.sibedge.ui.list.create.CreateItemFragment;
import com.acukanov.sibedge.ui.list.general.ListFragment;
import com.acukanov.sibedge.ui.main.MainActivity;
import com.acukanov.sibedge.ui.scaling.ScalingFragment;

import dagger.Component;

@PerActivity
@Component(dependencies = {ApplicationComponent.class}, modules = {ActivityModule.class})
public interface ActivityComponent {
    // Inject Activities
    void inject(MainActivity mainActivity);

    // Inject Fragments
    void inject(ListFragment listFragment);
    void inject(ScalingFragment scalingFragment);
    void inject(CreateItemFragment createItemFragment);
}
