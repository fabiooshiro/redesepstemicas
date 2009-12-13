package br.unicarioca.redesepistemicas.modelo;

public class Antecedente {
	private Double x;
	private Double y;
	private Double z;
	
	/**
	 * @return the x
	 */
	public Double getX() {
		return x;
	}
	/**
	 * @param x the x to set
	 */
	public void setX(Double x) {
		this.x = x;
	}
	/**
	 * @return the y
	 */
	public Double getY() {
		return y;
	}
	/**
	 * @param y the y to set
	 */
	public void setY(Double y) {
		this.y = y;
	}
	/**
	 * @return the z
	 */
	public Double getZ() {
		return z;
	}
	/**
	 * @param z the z to set
	 */
	public void setZ(Double z) {
		this.z = z;
	}
	public void add(Object valueAt) {
		if(x==null){
			x = Double.valueOf(valueAt.toString());
		}else if(y==null){
			y = Double.valueOf(valueAt.toString());
		}else{
			z = Double.valueOf(valueAt.toString());
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Antecedente))
			return false;
		Antecedente other = (Antecedente) obj;
		if (x == null) {
			if (other.x != null)
				return false;
		} else if (!x.equals(other.x))
			return false;
		if (y == null) {
			if (other.y != null)
				return false;
		} else if (!y.equals(other.y))
			return false;
		if (z == null) {
			if (other.z != null)
				return false;
		} else if (!z.equals(other.z))
			return false;
		return true;
	}
	
}
