package org.fleen.forsythia.app.spinner.spinnerVideoGenerators.spinner0000;

import java.util.Random;

import org.fleen.forsythia.app.spinner.core.Spinner;
import org.fleen.forsythia.app.spinner.core.Stripe;
import org.fleen.forsythia.app.spinner.core.StripeGenerator;
import org.fleen.forsythia.app.spinner.core.Stripe_ForsythiaComposition;

public class SG_ForsythiaComposition implements StripeGenerator{

  /*
   * ################################
   * SPINNER
   * ################################
   */
  
  Spinner spinner;
  
  public void setSpinner(Spinner spinner){
    this.spinner=spinner;}
  
  public Spinner getSpinner(){
    return spinner;}
  
  /*
   * ################################
   * STRIPE CREATOR
   * ################################
   */
  
  Random random=new Random();
  
  public Stripe getStripe(){
    Stripe a=new Stripe_ForsythiaComposition(fc);
    return a;}

}
