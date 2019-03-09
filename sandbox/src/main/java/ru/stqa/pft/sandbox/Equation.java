package ru.stqa.pft.sandbox;

public class Equation {

  private double a;
  private double b;
  private double c;

  private int n;

  public Equation (double a, double b, double c) {

    this.a = a;
    this.b = b;
    this.c = c;

    double d = b * b - 4 * a * c;

    if (a != 0) {                   //если a!=0 анализируем дискриминант
      if (d > 0) {
        n = 2;
      } else if (d == 0) {
        n = 1;
      } else {
        n = 0;
      }

    } else if (b != 0) {          //если a=0, b!=0, то n=1
      n = 1;

    } else if (c != 0) {          //если a=0, b=0, с!=0, то n=0
      n = 0;

    } else {
      n = -1;                      //если a,b,c = 0 решений бесконечно много, уравнение не имеет смысла
    }
  }

  public int rootNumber() {
    return n;
  }
}
