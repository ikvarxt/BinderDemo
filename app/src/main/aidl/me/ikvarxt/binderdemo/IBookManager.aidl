package me.ikvarxt.binderdemo;

import me.ikvarxt.binderdemo.Book;

interface IBookManager {

    void addBook(in Book book);

    List<Book> getBookList();
}