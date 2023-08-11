package com.chatbot.application.views;

import com.chatbot.application.views.chatbot.ChatbotView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

/**
 * 
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private H2 viewTitle;

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");
        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        H1 appName = new H1("ChatBot");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);
        Scroller scroller = new Scroller(createNavigation());

        // Add a link to the GitHub page in the footer
        Anchor githubLink = new Anchor("https://github.com/FlorianDevv", "GitHub");
        Image githubLogo = new Image("https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png",
                "GitHub logo");
        //<theme-editor-local-classname>
        githubLogo.addClassName("main-layout-img-1");
        githubLogo.setWidth("50px"); // Set the width of the image to 50 pixels
        Footer footer = createFooter();
        footer.add(githubLink, githubLogo);

        addToDrawer(header, scroller, footer);
    }

    private SideNav createNavigation() {

        SideNav nav = new SideNav();
        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();
        // <theme-editor-local-classname>
        layout.addClassName("main-layout-footer-1");
        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }

}