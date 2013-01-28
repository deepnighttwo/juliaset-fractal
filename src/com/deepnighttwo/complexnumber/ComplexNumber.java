/**
 * This source code belongs to Mark Zang, the author. To use it for
 * commercial/business purpose, please contact DeepNightTwo@gmail.com
 * 
 * @author Mark Zang
 * 
 */

package com.deepnighttwo.complexnumber;

public final strictfp class ComplexNumber {
    // real part of a complex number
    public double real;
    // imaginary part of a complex number
    public double imaginary;

    public ComplexNumber() {

    }

    public ComplexNumber(double realPart, double imaginaryPart) {
        this.real = realPart;
        this.imaginary = imaginaryPart;
    }

    public void setValue(double realPart, double imaginaryPart) {
        this.real = realPart;
        this.imaginary = imaginaryPart;
    }

    public ComplexNumber clone() {
        ComplexNumber clone = new ComplexNumber();
        clone.real = real;
        clone.imaginary = imaginary;
        return clone;
    }

    public void add(ComplexNumber c) {
        real += c.real;
        imaginary += c.imaginary;

    }

    public void sub(ComplexNumber c) {
        real -= c.real;
        imaginary -= c.imaginary;
    }

    public void square() {
        double tmpReal = real * real - imaginary * imaginary;
        double tmpimaginary = 2 * real * imaginary;
        real = tmpReal;
        imaginary = tmpimaginary;
    }

    public void multiply(ComplexNumber c) {
        double tmpReal = real * c.real - imaginary * c.imaginary;
        double tmpimaginary = real * c.imaginary + imaginary * c.real;
        real = tmpReal;
        imaginary = tmpimaginary;
    }

    public void divied(ComplexNumber c) {
        double tmpReal = (real * c.real + imaginary * c.imaginary) / (c.real * c.real + c.imaginary * c.imaginary);
        real = (imaginary * c.real - real * c.imaginary) / (c.real * c.real + c.imaginary * c.imaginary);
        real = tmpReal;
    }

    public String toString() {
        return "real: " + real + ", imaginary: " + imaginary;
    }

    public double size() {
        return real * real + imaginary * imaginary;
    }
}
