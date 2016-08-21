package com.patrickflorek.petrineteditor.view;

/**
 * Singleton
 */
public class Scale {
    private static Scale scale = null;
    private Double size = 1.0;

    /**
     * @return
     */
    protected static Scale getScale() {
        if (scale == null) {
            scale = new Scale();
        }
        return scale;
    }

    protected Double getSize() {
        return this.size;
    }

    protected void setSize(Double size) {
        this.size = size;

    }
}
