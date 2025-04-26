package tn.cloudnine.queute.dto.forum;

public class ToxicityReponse {
    private double identity_attack;
    private double insult;
    private double obscene;
    private double severe_toxicity;
    private double threat;
    private double toxicity;

    public double getIdentity_attack() {
        return identity_attack;
    }

    public void setIdentity_attack(double identity_attack) {
        this.identity_attack = identity_attack;
    }

    public double getInsult() {
        return insult;
    }

    public void setInsult(double insult) {
        this.insult = insult;
    }

    public double getObscene() {
        return obscene;
    }

    public void setObscene(double obscene) {
        this.obscene = obscene;
    }

    public double getSevere_toxicity() {
        return severe_toxicity;
    }

    public void setSevere_toxicity(double severe_toxicity) {
        this.severe_toxicity = severe_toxicity;
    }

    public double getThreat() {
        return threat;
    }

    public void setThreat(double threat) {
        this.threat = threat;
    }

    public double getToxicity() {
        return toxicity;
    }

    public void setToxicity(double toxicity) {
        this.toxicity = toxicity;
    }

    public ToxicityReponse(double identity_attack, double insult, double obscene, double severe_toxicity, double threat, double toxicity) {
        this.identity_attack = identity_attack;
        this.insult = insult;
        this.obscene = obscene;
        this.severe_toxicity = severe_toxicity;
        this.threat = threat;
        this.toxicity = toxicity;
    }

    public ToxicityReponse() {
    }
}
