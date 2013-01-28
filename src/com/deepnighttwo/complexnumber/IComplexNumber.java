/**
 * This source code belongs to Mark Zang, the author. To use it for
 * commercial/business purpose, please contact DeepNightTwo@gmail.com
 * 
 * @author Mark Zang
 * 
 */

package com.deepnighttwo.complexnumber;

public strictfp interface IComplexNumber {
    void add(IComplexNumber c);

    void sub(IComplexNumber c);

    void multiply(IComplexNumber c);

    void divied(IComplexNumber c);

    void square();

    double size();

    void setValue(double realPart, double imaginaryPart);

}
