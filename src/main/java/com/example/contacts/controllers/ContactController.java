package com.example.contacts.controllers;

import com.example.contacts.entities.Contact;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping
@Transactional
public class ContactController {

    private final SessionFactory sessionFactory;

    public ContactController(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("contact/index")
                .addObject("contacts", sessionFactory.getCurrentSession().createCriteria(Contact.class).list());
    }

    @GetMapping("create")
    public String create() {
        return "contact/create";
    }

    @PostMapping("create")
    public String create(Contact contact) {
        sessionFactory.getCurrentSession()
                .merge(contact);
        return "redirect:/";
    }

    @GetMapping("edit/{id}")
    public ModelAndView edit(@PathVariable int id) {
        return new ModelAndView("contact/edit")
                .addObject("contact", sessionFactory.getCurrentSession().createCriteria(Contact.class).add(Restrictions.eq("id", id)).uniqueResult());
    }

    @PostMapping("edit/{id}")
    public String edit(@PathVariable int id, Contact contact) {
        contact.setId(id);
        sessionFactory.getCurrentSession().merge(contact);
        return "redirect:/";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable int id) {
        sessionFactory.getCurrentSession().delete(sessionFactory.getCurrentSession().load(Contact.class, id));
        return "redirect:/";
    }
}
