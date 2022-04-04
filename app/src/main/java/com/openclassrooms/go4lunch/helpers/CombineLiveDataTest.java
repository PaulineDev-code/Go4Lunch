package com.openclassrooms.go4lunch.helpers;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import kotlin.Triple;

public class CombineLiveDataTest<A, B, C> extends MediatorLiveData<Pair<MediatorLiveData<Pair<A, B>>, C>> {
    private A a;
    private B b;
    private C c;
    private MediatorLiveData<Pair<A, B>> medAB;

    public CombineLiveDataTest(LiveData<A> ld1, LiveData<B> ld2, LiveData<C> ld3){

        setValue(Pair.create(medAB, c));

        addSource(ld1, (a) -> {
            if(a != null) {
                this.a = a;
            }
            medAB.setValue(Pair.create(a, b));
        });

        addSource(ld2, (b) -> {
            if(b != null) {
                this.b = b;
            }
            medAB.setValue(Pair.create(a, b));
        });

        addSource(ld3, (c) -> {
            if(c != null) {
                this.c = c;
            }
            setValue(Pair.create(medAB, c));
        });

    }
}
