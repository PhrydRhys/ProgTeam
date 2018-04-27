/*
 * Graciously borrowed from the NYU APS Spring 2017 course website
 */

public class Geometry {

	/*
	 * Point - Point class Required for most of the geometric algorithms below.
	 * convex hull, 3 point circle, intersection of circles, centroid/area of a
	 * polygon
	 * 
	 * @author Darko Aleksic
	 */
	static class Point {
		public static final double EPS = 1e-9;
		double x, y;

		/* add hashCode() and equals() if needed */
		Point(double x, double y) {
			this.x = x;
			this.y = y;
		}

		public double dist(Point p) {
			return Math.sqrt(distSquared(p));
		}

		public double distSquared(Point p) {
			double dx = x - p.x;
			double dy = y - p.y;
			return dx * dx + dy * dy;
		}

		// For debugging.
		public String toString() {
			return "(" + x + "," + y + ")";
		}

		public int compareTo(Point p2) {
			if (Math.abs(y - p2.y) < EPS) {
				if (Math.abs(x - p2.x) < EPS)
					return 0;
				if (x < p2.x)
					return -1;
				return 1;
			}
			if (y < p2.y)
				return -1;
			return 1;
		}

		public int compareTo(Point p2, Point pivot) {
			if (Math.abs(y - pivot.y) < EPS && Math.abs(y - p2.y) < EPS) {
				if (Math.abs(x - p2.x) < EPS)
					return 0;
				if (x > p2.x) // !!
					return -1;
				return 1;
			}
			double k = sub(pivot).cross(p2.sub(pivot));
			if (Math.abs(k) < EPS) {
				double d = distSquared(pivot) - p2.distSquared(pivot);
				if (Math.abs(d) < EPS)
					return 0;
				if (d < 0)
					return -1;
				return 1;
			}
			if (k < 0)
				return -1;
			return 1;
		}

		public Point sub(Point p2) {
			return new Point(x - p2.x, y - p2.y);
		}

		public double dot(Point p2) {
			return x * p2.x + y * p2.y;
		}

		public double cross(Point p2) {
			return x * p2.y - p2.x * y;
		}

		/*
		 * centroid - they must be in order (CW or CCW, does not matter)
		 */
		public static Point centroid(Point[] p, int n) {
			Point c = new Point(0, 0);
			int i, j;
			double sum = 0;
			double area = 0;
			for (i = n - 1, j = 0; j < n; i = j++) {
				area = p[i].x * p[j].y - p[i].y * p[j].x;
				sum += area;
				c.x += (p[i].x + p[j].x) * area;
				c.y += (p[i].y + p[j].y) * area;
			}
			sum *= 3.0;
			c.x /= sum;
			c.y /= sum;
			return c;
		}

		// -------------------------------------------------------------

		private static void angularSort(Point ps[], int begin, int end) {
			int mid;
			if (end - begin <= 1) {
				return;
			}
			mid = (begin + end) / 2;
			angularSort(ps, begin, mid);
			angularSort(ps, mid, end);
			merge(ps, begin, mid, end);
		}

		private static void merge(Point[] ps, int start, int mid, int end) {
			int i = start;
			int j = mid;
			int k = 0;
			Point[] temp = new Point[end - start];
			while ((i < mid) && (j < end))
				if (ps[i].compareTo(ps[j], ps[0]) <= 0) {
					temp[k++] = ps[i++];
				} else {
					temp[k++] = ps[j++];
				}
			while (i < mid) {
				temp[k++] = ps[i++];
			}
			while (j < end) {
				temp[k++] = ps[j++];
			}
			for (i = start; i < end; i++)
				ps[i] = temp[i - start];
		}

		// -------------------------------------------------------------

		/**
		 * @param ps
		 *            array containing the set of distinct points
		 * @param n
		 *            number of points
		 * @return array of points on the convex hull (may be empty, if n<=0)
		 */
		public static Point[] grahamScan(Point[] ps, int n, boolean keepColinear) {
			// maybe check for these outside?
			if (n <= 0) {
				Point[] ret = new Point[0];
				return ret; // or null?
			}
			if (n == 1) {
				Point[] ret = new Point[1];
				ret[0] = ps[0];
				return ret;
			}
			// find pivot and sort
			int p = 0;
			for (int i = 1; i < n; i++) {
				if (ps[i].compareTo(ps[p]) < 0)
					p = i;
			}
			Point tmp = ps[0];
			ps[0] = ps[p];
			ps[p] = tmp;
			angularSort(ps, 1, n);
			// check if they are all on the same line
			if (Math.abs((ps[n - 1].sub(ps[0])).cross(ps[1].sub(ps[0]))) < EPS) {
				if (keepColinear) {
					Point[] ret = new Point[n];
					ret[0] = ps[0];
					if (ps[0].distSquared(ps[1]) >= ps[0]
							.distSquared(ps[n - 1]) + EPS)
						for (int i = 1; i < n; i++)
							ret[n - i] = ps[i];
					else
						for (int i = 1; i < n; i++)
							ret[i] = ps[i];
					return ret;
				} else {
					Point[] ret = new Point[2];
					ret[0] = ps[0];
					if (ps[0].distSquared(ps[1]) >= ps[0]
							.distSquared(ps[n - 1]) + EPS)
						ret[1] = ps[1];
					else
						ret[1] = ps[n - 1];
					return ret;
				}
			}
			// remove closer ones on the same line
			Point[] tps = new Point[n];
			tps[0] = ps[0];
			tps[1] = ps[1];
			int tt = 0;
			int start = 2;
			int end = n;
			if (keepColinear) {
				Point a = ps[0].sub(ps[1]);
				while (Math.abs(a.cross(ps[0].sub(ps[start]))) < EPS) {
					tps[start] = ps[start];
					start++;
				}
				a = ps[0].sub(ps[n - 1]);
				while (Math.abs(a.cross(ps[0].sub(ps[end - 1]))) < EPS) {
					end--;
				}
				end++;
			}
			for (int i = start; i < end; i++) {
				Point a = tps[i - tt - 1].sub(tps[i - tt - 2]);
				Point b = ps[i].sub(tps[i - tt - 2]);
				if (!keepColinear && Math.abs(a.cross(b)) < EPS) {
					tps[i - tt - 1] = ps[i];
					tt++;
				} else {
					tps[i - tt] = ps[i];
				}
			}
			for (int i = end; i < n; i++) {
				tps[i - tt] = ps[i];
			}
			// remove last point if colinear
			if (!keepColinear && n - tt > 2) {
				Point a = tps[0].sub(tps[n - tt - 2]);
				Point b = tps[0].sub(tps[n - tt - 1]);
				if (Math.abs(a.cross(b)) < EPS)
					tt++;
			}
			n -= tt;
			Point[] stack = new Point[n];
			int stackSize = 0;
			stack[stackSize++] = tps[0];
			stack[stackSize++] = tps[1];
			for (int i = 2; i < n; i++) {
				while (true) {
					Point a = stack[stackSize - 1].sub(stack[stackSize - 2]);
					Point b = tps[i].sub(stack[stackSize - 2]);
					double cross = a.cross(b);
					if (cross <= -EPS || (cross < EPS && keepColinear))
						break;
					stackSize--;
				}
				stack[stackSize++] = tps[i];
			}
			Point[] ret = new Point[stackSize];
			System.arraycopy(stack, 0, ret, 0, stackSize);
			return ret;
		}
	}

}
