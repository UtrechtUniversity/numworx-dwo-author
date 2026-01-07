package fi.wiskopdr.opdrnav;

import java.math.RoundingMode;
import java.text.DecimalFormatSymbols;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Currency;

@SuppressWarnings("serial")
/**
 * Decorator pattern.
 */
public class NumberDecorator extends NumberFormat {

  final NumberFormat delegate;

  public boolean isParseIntegerOnly() {
    return delegate.isParseIntegerOnly();
  }

  public void setParseIntegerOnly(boolean value) {
    delegate.setParseIntegerOnly(value);
  }

  public boolean isGroupingUsed() {
    return delegate.isGroupingUsed();
  }

  public void setGroupingUsed(boolean newValue) {
    delegate.setGroupingUsed(newValue);
  }

  public int getMaximumIntegerDigits() {
    return delegate.getMaximumIntegerDigits();
  }

  public void setMaximumIntegerDigits(int newValue) {
    delegate.setMaximumIntegerDigits(newValue);
  }

  public int getMinimumIntegerDigits() {
    return delegate.getMinimumIntegerDigits();
  }

  public void setMinimumIntegerDigits(int newValue) {
    delegate.setMinimumIntegerDigits(newValue);
  }

  public int getMaximumFractionDigits() {
    return delegate.getMaximumFractionDigits();
  }

  public void setMaximumFractionDigits(int newValue) {
    delegate.setMaximumFractionDigits(newValue);
  }

  public int getMinimumFractionDigits() {
    return delegate.getMinimumFractionDigits();
  }

  public void setMinimumFractionDigits(int newValue) {
    delegate.setMinimumFractionDigits(newValue);
  }

  public Currency getCurrency() {
    return delegate.getCurrency();
  }

  public void setCurrency(Currency currency) {
    delegate.setCurrency(currency);
  }

  public RoundingMode getRoundingMode() {
    return delegate.getRoundingMode();
  }

  public void setRoundingMode(RoundingMode roundingMode) {
    delegate.setRoundingMode(roundingMode);
  }

  final char decimalseparator;
  
  public NumberDecorator(NumberFormat format) {
    this.delegate = format;
    setGroupingUsed(false);
    decimalseparator = DecimalFormatSymbols.getInstance().getDecimalSeparator();
  }

  @Override
  public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
    return delegate.format(number, toAppendTo, pos);
  }

  @Override
  public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
    return delegate.format(number, toAppendTo, pos);
  }

  @Override
  public Number parse(String source, ParsePosition parsePosition) {
    source = source.replace('.', decimalseparator);
    return delegate.parse(source, parsePosition);
  }

}
