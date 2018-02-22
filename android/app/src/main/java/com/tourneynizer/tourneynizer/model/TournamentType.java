package com.tourneynizer.tourneynizer.model;

import android.support.annotation.NonNull;
import android.text.Editable;

/**
 * Created by ryanl on 2/3/2018.
 */

public enum TournamentType {
    VOLLEYBALL_POOLED,
    VOLLEYBALL_BRACKET;

    @Override
    public @NonNull String toString() {
        String name = super.toString();
        name = name.replace("_", " ");
        String[] words = name.split(" ");
        Editable result = Editable.Factory.getInstance().newEditable(words[0].substring(0, 1) + words[0].substring(1).toLowerCase());
        for (int i = 1; i < words.length; i++) {
            result.append(" ").append(words[i].substring(0, 1)).append(words[i].substring(1).toLowerCase());
        }
        return result.toString();
    }
}
