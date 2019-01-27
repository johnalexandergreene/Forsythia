package org.fleen.forsythia.app.spinner.core;

public interface StripeGenerator{
  
  void setSpinner(Spinner spinner);
  
  Spinner getSpinner();
  
  Stripe getStripe();

}
