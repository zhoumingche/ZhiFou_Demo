package com.lovejjfg.zhifou.presenters;/*
 * Copyright (C) 2014 Jorge Castillo Pérez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import rx.Subscriber;
import rx.Subscription;

/**
 * Android lifecycle callbacks from activities/fragments are linked to the Presenters by this 
 * behavior interface. Linking callbacks is declared as an optional behavior and not a need for
 * every presenter.
 *
 * @author Jorge Castillo Pérez
 */
public interface BasePresenter {
    void onStart();
    void onResume();
    void onDestroy();

    void subscribe(Subscription subscriber);

    void unSubscribe();
}
