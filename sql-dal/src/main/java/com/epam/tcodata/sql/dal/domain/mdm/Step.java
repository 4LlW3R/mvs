package com.epam.tcodata.sql.dal.domain.mdm;

import com.epam.tcodata.sql.dal.IStorable;
import com.epam.tcodata.sql.dal.domain.PrimaryKey;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Raw class that represents one record in steps table.
 */
public class Step implements IStorable {

    public static class Fields {
        public static final String ID = "id";
        public static final String PARENT_ID = "parent_id";
        public static final String ORD = "ord";
        public static final String RULE_TYPE = "rule_type";
        public static final String NATURAL_KEY_NAME = "natural_key_name";
        public static final String BODY = "body";
        public static final String PAR0 = "par0";
        public static final String PAR1 = "par1";
        public static final String PAR2 = "par2";
        public static final String PAR3 = "par3";
        public static final String PAR4 = "par4";
        public static final String PAR5 = "par5";
        public static final String PAR6 = "par6";
        public static final String PAR7 = "par7";
        public static final String PAR8 = "par8";
        public static final String PAR9 = "par9";
        private Fields(){   /***  Default implementation ***/  }
    }

    @PrimaryKey
    @ColumnName(Fields.ID)
    private long id;

    @ColumnName(Fields.PARENT_ID)
    private long parentId;

    @ColumnName(Fields.ORD)
    private int ord;

    @ColumnName(Fields.RULE_TYPE)
    private String ruleType;

    @ColumnName(Fields.NATURAL_KEY_NAME)
    private String naturalKeyName;

    @ColumnName(Fields.BODY)
    private String body;

    @ColumnName(Fields.PAR0)
    private String par0;

    @ColumnName(Fields.PAR1)
    private String par1;

    @ColumnName(Fields.PAR2)
    private String par2;

    @ColumnName(Fields.PAR3)
    private String par3;

    @ColumnName(Fields.PAR4)
    private String par4;

    @ColumnName(Fields.PAR5)
    private String par5;

    @ColumnName(Fields.PAR6)
    private String par6;

    @ColumnName(Fields.PAR7)
    private String par7;

    @ColumnName(Fields.PAR8)
    private String par8;

    @ColumnName(Fields.PAR9)
    private String par9;

    public Step() {
        this(-1, null);
    }

    public Step(int ord, String ruleType) {
        this.ord = ord;
        this.ruleType = ruleType;
    }

    /**
     * Public constructor.
     *
     * @param ruleId     - code of RULE.
     * @param body       - content of query/expression etc.
     * @param parameters - parameters names.
     */
    public Step(long ruleId, String ruleType, String natuaralKeyName, int ord, String body, String... parameters) {
        this.id = -1;
        this.parentId = ruleId;
        this.ruleType = ruleType;
        this.naturalKeyName = natuaralKeyName;
        this.ord = ord;
        this.body = body;

        switch (parameters.length) {
            case 10:
                this.par9 = parameters[9];
                break;
            case 9:
                this.par8 = parameters[8];
                break;
            case 8:
                this.par7 = parameters[7];
                break;
            case 7:
                this.par6 = parameters[6];
                break;
            case 6:
                this.par5 = parameters[5];
                break;
            case 5:
                this.par4 = parameters[4];
                break;
            case 4:
                this.par3 = parameters[3];
                break;
            case 3:
                this.par2 = parameters[2];
                break;
            case 2:
                this.par1 = parameters[1];
                break;
            case 1:
                this.par0 = parameters[0];
                break;
            case 0:
                break;
            default:
        }
    }

    @Override
    public String toString() {
        return "Step{"
                + "id=" + id
                + ", parent_id=" + parentId
                + ", ord=" + ord
                + ", rule_type=" + ruleType
                + ", body='" + body + '\''
                + ", parameters=" + listParameters()
                + '}';
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public long getParentId() {
        return this.parentId;
    }

    @Override
    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public int getOrd() {
        return ord;
    }

    public void setOrd(int ord) {
        this.ord = ord;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public String getNaturalKeyName() {
        return naturalKeyName;
    }

    public void setNaturalKeyName(String naturalKeyName) {
        this.naturalKeyName = naturalKeyName;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    /**
     * A getter that return a field value by the field index.
     *
     * @param n - index of the field.
     * @return value of the field.
     */
    public String parameter(int n) {
        switch (n) {
            case 0:
                return this.par0;
            case 1:
                return this.par1;
            case 2:
                return this.par2;
            case 3:
                return this.par3;
            case 4:
                return this.par4;
            case 5:
                return this.par5;
            case 6:
                return this.par6;
            case 7:
                return this.par7;
            case 8:
                return this.par8;
            case 9:
                return this.par9;
            default:
                throw new IllegalArgumentException("" + n + " exceeds max param number");
        }
    }

    /**
     * A setter that stands a value for a field by its index.
     *
     * @param n     - index of the field.
     * @param value - value for the field.
     */
    public void parameter(int n, String value) {
        switch (n) {
            case 0:
                this.par0 = value;
                break;
            case 1:
                this.par1 = value;
                break;
            case 2:
                this.par2 = value;
                break;
            case 3:
                this.par3 = value;
                break;
            case 4:
                this.par4 = value;
                break;
            case 5:
                this.par5 = value;
                break;
            case 6:
                this.par6 = value;
                break;
            case 7:
                this.par7 = value;
                break;
            case 8:
                this.par8 = value;
                break;
            case 9:
                this.par9 = value;
                break;
            default:
                throw new IllegalStateException("" + n + " exceeds max param number");
        }
    }

    /**
     * Gets all parameters names for this step.
     *
     * @return list of parameters.
     */
    public List<String> listParameters() {
        return Arrays.asList(
                this.par0,
                this.par1,
                this.par2,
                this.par3,
                this.par4,
                this.par5,
                this.par6,
                this.par7,
                this.par8,
                this.par9
        );
    }

    public String getPar0() {
        return par0;
    }

    public void setPar0(String par0) {
        this.par0 = par0;
    }

    public String getPar1() {
        return par1;
    }

    public void setPar1(String par1) {
        this.par1 = par1;
    }

    public String getPar2() {
        return par2;
    }

    public void setPar2(String par2) {
        this.par2 = par2;
    }

    public String getPar3() {
        return par3;
    }

    public void setPar3(String par3) {
        this.par3 = par3;
    }

    public String getPar4() {
        return par4;
    }

    public void setPar4(String par4) {
        this.par4 = par4;
    }

    public String getPar5() {
        return par5;
    }

    public void setPar5(String par5) {
        this.par5 = par5;
    }

    public String getPar6() {
        return par6;
    }

    public void setPar6(String par6) {
        this.par6 = par6;
    }

    public String getPar7() {
        return par7;
    }

    public void setPar7(String par7) {
        this.par7 = par7;
    }

    public String getPar8() {
        return par8;
    }

    public void setPar8(String par8) {
        this.par8 = par8;
    }

    public String getPar9() {
        return par9;
    }

    public void setPar9(String par9) {
        this.par9 = par9;
    }

    public static class StepComparator implements Comparator<Step>, Serializable {

        @Override
        public int compare(Step o1, Step o2) {
            return o1.getOrd() - o2.getOrd();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Step step = (Step) o;
        return parentId == step.parentId
                && ord == step.ord
                && Objects.equals(ruleType, step.ruleType)
                && Objects.equals(naturalKeyName, step.naturalKeyName)
                && Objects.equals(body, step.body)
                && Objects.equals(par0, step.par0)
                && Objects.equals(par1, step.par1)
                && Objects.equals(par2, step.par2)
                && Objects.equals(par3, step.par3)
                && Objects.equals(par4, step.par4)
                && Objects.equals(par5, step.par5)
                && Objects.equals(par6, step.par6)
                && Objects.equals(par7, step.par7)
                && Objects.equals(par8, step.par8)
                && Objects.equals(par9, step.par9);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parentId, ord, ruleType, naturalKeyName, body, par0, par1, par2, par3, par4, par5, par6, par7, par8, par9);
    }
}
