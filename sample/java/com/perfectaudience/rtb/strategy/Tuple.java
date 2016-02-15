package com.perfectaudience.rtb.collections;

public enum Tuple {
    ; // Enum singleton

    private static abstract class AbstractTuple {
        public final int numElements;

        private AbstractTuple(final int numElements) {
            this.numElements = numElements;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + numElements;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            AbstractTuple other = (AbstractTuple) obj;
            if (numElements != other.numElements)
                return false;
            return true;
        }
    }

    public static <AI extends A, A, BI extends B, B> Pair<A, B> getPair(final AI a, final BI b) {
        return new Pair<>(a, b);
    }

    public static <A, B, C> Triple<A, B, C> getTriple(final A a, final B b, final C c) {
        return new Triple<>(a, b, c);
    }

    public static class Pair<A, B> extends AbstractTuple {
        public final A a;
        public final B b;

        private <AI extends A, BI extends B> Pair(final AI a, final BI b) {
            super(2);
            this.a = a;
            this.b = b;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + ((a == null) ? 0 : a.hashCode());
            result = prime * result + ((b == null) ? 0 : b.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (!super.equals(obj))
                return false;
            if (getClass() != obj.getClass())
                return false;
            @SuppressWarnings("rawtypes")
            // Cannot use typed cast between Tuples of same class but diff type
            Pair other = (Pair) obj;
            if (a == null) {
                if (other.a != null)
                    return false;
            } else if (!a.equals(other.a))
                return false;
            if (b == null) {
                if (other.b != null)
                    return false;
            } else if (!b.equals(other.b))
                return false;
            return true;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("Pair [a=");
            builder.append(a);
            builder.append(", b=");
            builder.append(b);
            builder.append("]");
            return builder.toString();
        }
    }

    public static class Triple<A, B, C> extends AbstractTuple {
        public final A a;
        public final B b;
        public final C c;

        private Triple(final A a, final B b, final C c) {
            super(3);
            this.a = a;
            this.b = b;
            this.c = c;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + ((a == null) ? 0 : a.hashCode());
            result = prime * result + ((b == null) ? 0 : b.hashCode());
            result = prime * result + ((c == null) ? 0 : c.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (!super.equals(obj))
                return false;
            if (getClass() != obj.getClass())
                return false;
            @SuppressWarnings("rawtypes")
            // Cannot use typed cast between Tuples of same class but diff type
            Triple other = (Triple) obj;
            if (a == null) {
                if (other.a != null)
                    return false;
            } else if (!a.equals(other.a))
                return false;
            if (b == null) {
                if (other.b != null)
                    return false;
            } else if (!b.equals(other.b))
                return false;
            if (c == null) {
                if (other.c != null)
                    return false;
            } else if (!c.equals(other.c))
                return false;
            return true;
        }
    }
}
