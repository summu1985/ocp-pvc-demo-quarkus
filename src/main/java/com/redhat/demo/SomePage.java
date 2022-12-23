package com.redhat.demo;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.*;

import static java.util.Objects.requireNonNull;

@Path("/pvcdemo")
public class SomePage {

    @Inject
    Template page;

    private String fileName = "/tmp/bgcolor.properties";

    public SomePage(Template page) {
        this.page = requireNonNull(page, "page is required");
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get(@QueryParam("name") String name) {
        String bgcolor = "peachpuff";
        try {
            bgcolor = readFromFile();
            System.out.println("Bgcolor = " + bgcolor);

        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return page.data("name", name).data("bgcolor", bgcolor);

    }

    @Path("/save")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance save(@QueryParam("bgcolor") String bgcolor) {
        try {
            writeToFile("bgcolor=" + bgcolor);
            System.out.println("Writing");
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return page.data("name", bgcolor).data("bgcolor", bgcolor);
    }

    public void writeToFile(String string)
            throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(string);
        writer.close();
    }

    private String readFromFile()
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString().split("=")[1];
    }

}
