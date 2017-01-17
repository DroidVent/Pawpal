package com.org.pawpal.interfaces;

/**
 * Created by hp-pc on 16-01-2017.
 */

public interface OnInboxListener {
    void onStarClicked(int position);
    void onArchieveClicked(int position);
    void onItemClicked(int position);
}
