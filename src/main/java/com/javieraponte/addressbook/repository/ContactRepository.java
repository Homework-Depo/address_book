package com.javieraponte.addressbook.repository;

import com.javieraponte.addressbook.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    public List<Contact> findAllByOrderByIdAsc();
}