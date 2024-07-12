package com.example.Mosaic.mosaic.development;

public enum Template {
    FLOWERS("C:\\\\Users\\\\Kirill\\\\Desktop\\\\РГРТУ\\\\3 курс\\\\2 семестр\\\\НИР\\\\Датасеты\\\\цветы"),
    SOMETHING("something");

    private String template;

    Template(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }

}
