package com.example.ato;

/** Tariffe 2026 per utenza domestica residente, ATO Mantova. */
public final class TariffCalculator2026 {
    public enum Area { AQA_EX_TEA, SICAM }

    private static final double[] AQA_ACQUEDOTTO = {0.471355, 0.942710, 1.084117, 1.414066};
    private static final double[] SICAM_ACQUEDOTTO = {0.565923, 1.131845, 1.143164, 1.154483};

    private TariffCalculator2026() { }

    public static Estimate calculate(Area area, double cubicMeters) {
        double volume = Math.max(0, cubicMeters);
        double[] waterRates = area == Area.SICAM ? SICAM_ACQUEDOTTO : AQA_ACQUEDOTTO;
        double water = tieredWater(volume, waterRates);
        double sewer = volume * (area == Area.SICAM ? 0.345774 : 0.371075);
        double treatment = volume * (area == Area.SICAM ? 0.948291 : 0.911576);
        double fixed = area == Area.SICAM ? 38.0 : 49.778292;
        return new Estimate(water, sewer, treatment, fixed, water + sewer + treatment + fixed);
    }

    private static double tieredWater(double m3, double[] rates) {
        double[] limits = {55, 125, 60, Double.MAX_VALUE};
        double remaining = m3, total = 0;
        for (int i = 0; i < rates.length && remaining > 0; i++) {
            double inTier = Math.min(remaining, limits[i]);
            total += inTier * rates[i];
            remaining -= inTier;
        }
        return total;
    }

    public static String tierDescription(double m3) {
        if (m3 <= 55) return "Tutta la fornitura resta nella fascia agevolata (0-55 m³).";
        if (m3 <= 180) return "Hai usato anche la fascia base (56-180 m³).";
        if (m3 <= 240) return "Sei entrato nella prima fascia di eccedenza (181-240 m³).";
        return "Sei nella seconda fascia di eccedenza (oltre 240 m³).";
    }

    public static final class Estimate {
        public final double water, sewer, treatment, fixed, subtotal;
        Estimate(double water, double sewer, double treatment, double fixed, double subtotal) {
            this.water = water; this.sewer = sewer; this.treatment = treatment;
            this.fixed = fixed; this.subtotal = subtotal;
        }
    }
}
