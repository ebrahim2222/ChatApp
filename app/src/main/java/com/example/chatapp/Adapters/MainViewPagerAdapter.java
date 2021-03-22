package com.example.chatapp.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.chatapp.Fragments.ChatFragmet;
import com.example.chatapp.Fragments.ContactsFragment;
import com.example.chatapp.Fragments.GroupChatFragment;

public class MainViewPagerAdapter extends FragmentPagerAdapter {
    public MainViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }
    Fragment [] fragments = {new ChatFragmet(),new GroupChatFragment(),new ContactsFragment()};
    String [] titles = {"Chat","Group Chat","Contacts"};
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
