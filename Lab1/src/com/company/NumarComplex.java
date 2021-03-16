package com.company;
import java.util.Objects;

public class NumarComplex {
    private float re, im;

    /**
     * Constructor
     *
     * @param re: float
     * @param im: float
     */
    public NumarComplex(float re, float im) {
        this.re = re;
        this.im = im;
    }

    /**
     * Metoda pentru adunarea a doua numere complexe
     *
     * @param ot: NumarComplex
     *            post: this' = this + ot
     */
    public NumarComplex adunare(NumarComplex ot) {
        this.re += ot.re;
        this.im += ot.im;
        return this;
    }

    /**
     * Metoda pentru scaderea a doua numere complexe
     *
     * @param ot: NumarComplex
     *            post: this' = this - ot
     */
    public NumarComplex scadere(NumarComplex ot) {
        this.re -= ot.re;
        this.im -= ot.im;
        return this;
    }

    /**
     * Metoda pentru inmultirea a doua numere complexe
     *
     * @param ot: NumarComplex
     *            post: this' = this * ot
     */
    public NumarComplex inmultire(NumarComplex ot) {
        var real = this.re * ot.re - this.im * ot.im;
        this.im = this.re * ot.im + this.im * ot.re;
        this.re = real;
        return this;
    }

    /**
     * Metoda pentru impartirea a doua numere complexe
     *
     * @param ot: NumarComplex
     *            post: this' = this * ot
     */
    public NumarComplex impartire(NumarComplex ot) {
        var numitor = ot.re * ot.re + ot.im * ot.im;
        inmultire(ot.conjugat());
        this.re /= numitor;
        this.im /= numitor;
        return this;
    }

    /**
     * Getter pentru partea reala a unui numar complex
     *
     * @return re: float, partea reala a numarului complex curent
     */
    public float get_real() {
        return this.re;
    }

    /**
     * Getter pentru partea imaginara a unui numar complex
     *
     * @return re: float, partea imaginara a numarului complex curent
     */
    public float get_imaginar() {
        return this.im;
    }

    /**
     * Metoda pentru obtinerea conjugatului unui numar complex
     *
     * @return c: NumarComplex, c = this conjugat
     */
    public NumarComplex conjugat() {
        return new NumarComplex(this.re, -1 * this.im);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumarComplex that = (NumarComplex) o;
        return Float.compare(that.re, re) == 0 &&
                Float.compare(that.im, im) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(re, im);
    }

    @Override
    public String toString() {
        return this.get_real() + " + " + this.get_imaginar() + "*i";
    }
}
