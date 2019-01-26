package org.fleen.forsythia.app.spinner.spinnerVideoGenerators.spinner0000;

import org.fleen.forsythia.app.spinner.core.Spinner;
import org.fleen.forsythia.app.spinner.core.Stripe;
import org.fleen.forsythia.app.spinner.core.StripeChainWithMovingViewport;
import org.fleen.forsythia.app.spinner.core.StripeGenerator;
import org.fleen.forsythia.app.spinner.core.Stripe_ColoredBox;

public class SG_ColoredBoxes implements StripeGenerator{

  Spinner spinner;
  
  void setSpinner(Spinner spinner){
    this.spinner=spinner;}
  
  public Stripe getStripe(){
    Stripe a=new Stripe_ColoredBox(spinner.chain,100);
    return a;}

}
