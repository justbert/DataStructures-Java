import geodetic.Coordinate;
import geodetic.GeodeticCalculator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;

import com.sun.org.apache.bcel.internal.classfile.Code;

/** Basic binary search tree that supports insertion and three traversal patterns. */
public class QuadTree {
	private QuadNode root;
	private int maxItemsBeforeSplit;
	private int size;
	
	public QuadTree(Coordinate topLeft, Coordinate bottomRight,int maxItemsBeforeSplit) {
		this.root = new QuadNode(topLeft, bottomRight);
		this.maxItemsBeforeSplit = maxItemsBeforeSplit;
	}
	
	public QuadTree(Coordinate topLeft, Coordinate bottomRight) {
		this(topLeft, bottomRight, 5);
	}
	
	public boolean add(PostalCode code) {
		return root.add(code);
	}
	
	
  // Beautiful illustration of the IN-ORDER RECURSIVE ALGORITHM
//	public void displayInOrder() { System.out.print("\nIn-Order Traversal: "); displayInOrder(root); }
//	private void displayInOrder(Node node) {
//		if (node == null)
//			return;
//		displayInOrder(node.nw);
//		System.out.print(node);  // The ESSENCE of IN-ORDER: Act on the node AFTER finishing all LEFT branch processing, but BEFORE embarking RIGHT branch processing.
//		displayInOrder(node.ne);
//	} // end displayInOrder()
	
  // Beautiful illustration of the PRE-ORDER RECURSIVE ALGORITHM
	public void displayPreOrder() { System.out.print("\nPre-Order Traversal: "); displayPreOrder(root); }
	private void displayPreOrder(QuadNode node) {
		if (node == null)
			return;
		System.out.print(node); // The ESSENCE of PRE-ORDER: Act on the node BEFORE embarking on any LEFT or RIGHT branch processing.
		displayPreOrder(node.nw);
		displayPreOrder(node.ne);
		displayPreOrder(node.sw);
		displayPreOrder(node.se);
	} // end displayPreOrder()
	
  // Beautiful illustration of the POST-ORDER RECURSIVE ALGORITHM
	public void displayPostOrder() { System.out.print("\nPost-Order Traversal: "); displayPostOrder(root); }
	private void displayPostOrder(QuadNode node) {
		if (node == null)
			return;
		displayPostOrder(node.nw);
		displayPostOrder(node.ne);
		displayPostOrder(node.sw);
		displayPostOrder(node.se);
		System.out.print(node); // The ESSENCE of POST-ORDER: Act on the node AFTER finishing all LEFT and RIGHT branch processing.
	} // end displayPostOrder()
	
	@Override
	public String toString() {
		return "Binary Search Tree: size:" + size;
	}
	
	/**
	 * A given Coordinate will be searched in the QuadTree.
	 * @param targetCoord
	 * @return Returns a reference-to the closest matching PostalCode.
	 */
	public LinkedList<PostalCode> findClosest(Coordinate targetCoord) {
		return root.findClosest3(targetCoord); // TODO You have work to do here!!!!!!!!!!!!!!
	}
	
	
	// ***************************************************************************
	// ***************************************************************************
	// Start inner class Node
	// Inner class to support the storage of items to be added to the tree, in this case, "int" values 
	private class QuadNode {
		private Coordinate topLeftCoordinate;
		private Coordinate bottomRightCoordinate;
		Coordinate coordCenter;
		private HashMap<Coordinate, LinkedList<PostalCode>> codeCollection; // The use of "private" is technically NOT required, because they are specified in a "private" class.
		private QuadNode nw;  // Even though "private" the parent (containing) class has full rights of access.
		private QuadNode ne; // Using "private" is probably "better" form, even though not required.
		private QuadNode sw;  // Even though "private" the parent (containing) class has full rights of access.
		private QuadNode se; // Using "private" is probably "better" form, even though not required.
		
		private QuadNode(Coordinate topLeft, Coordinate bottomRight) {
			topLeftCoordinate = topLeft;
			bottomRightCoordinate = bottomRight;
			coordCenter = new Coordinate((topLeftCoordinate.getLatitude()+bottomRightCoordinate.getLatitude())/2.0, (topLeftCoordinate.getLongitude()+bottomRightCoordinate.getLongitude())/2.0 );
			codeCollection = new HashMap<>();
		}
		
		/**
		 * @param newCode
		 * @return
		 */
//		public boolean add(PostalCode newCode) {
//			if(nodeCoordinate == null) {
//				this.nodeCoordinate = newCode.getCoordinate();
//				
//				if(sameCoordinatesCollection == null)
//					sameCoordinatesCollection = new LinkedList<>();
//				
//				sameCoordinatesCollection.add(newCode);
//				
//				++size;
//				return true;
//			}else if(newCode.getCoordinate().equals(nodeCoordinate)) {
//				//Case 0: Is the same as the current postalCode				
//				this.sameCoordinatesCollection.add(newCode);
//				++size;
//				return true;
//			} else if(codeCollection == null) {
//				//Case 1: Already an internal node, I cannot store the postal code. Suggests recursion.
//				//if true, INTERNAL node, does not accept code children.
//				//recursively  add to child QuadNode
//				//All four quadNodes MUST exist (nw, ne, sw, se)
//				//In which quadrant are the codes distributed
//				
//				addToCorrectQuadrant(newCode);
//				++size;
//				return true;				
//			} else { //LEAF node because collection of PCs is 
//				//Case 2a: It's a leaf node, Currently storing postal codes, and has room. EASY: Just add
//				if(codeCollection.size() < maxItemsBeforeSplit) {
//					codeCollection.add(newCode);
//					++size;
//					return true;
//				} else {
//					//Case 2b: It's a leaf node, Currently storing postal codes, and has NO room. Hard: Must Split.
//					ne = new QuadNode();
//					nw = new QuadNode();
//					se = new QuadNode();
//					sw = new QuadNode();
//					for(PostalCode existingCode : codeCollection) {
//						addToCorrectQuadrant(existingCode);
//					}
//					addToCorrectQuadrant(newCode);
//					codeCollection = null;
//					++size;
//					return true;
//				}
//			}
//		}
		
		public boolean add(PostalCode newCode) {
			if(codeCollection == null && ne != null) {
				return addToCorrectQuadrant(newCode);
			} else {
				if(codeCollection.size() == 0)
					this.coordCenter = newCode.getCoordinate();
				if(codeCollection.get(newCode.getCoordinate()) != null) {
					codeCollection.get(newCode.getCoordinate()).add(newCode);
				} else {
					LinkedList<PostalCode> entries = new LinkedList<>();
					entries.add(newCode);
					codeCollection.put(newCode.getCoordinate(), entries);
				}
				
				if(codeCollection.size() < maxItemsBeforeSplit) {
					++size;
					return true;
				} else {
					ne = new QuadNode(new Coordinate(topLeftCoordinate.getLatitude(), coordCenter.getLongitude()), new Coordinate(coordCenter.getLatitude(), bottomRightCoordinate.getLongitude()));
					nw = new QuadNode(topLeftCoordinate, coordCenter);
					se = new QuadNode(coordCenter, bottomRightCoordinate);
					sw = new QuadNode(new Coordinate(coordCenter.getLatitude(), topLeftCoordinate.getLongitude()), new Coordinate(bottomRightCoordinate.getLatitude(), coordCenter.getLongitude()));
					
					Set<Entry<Coordinate, LinkedList<PostalCode>>> entries = codeCollection.entrySet();
					
					for(Entry<Coordinate, LinkedList<PostalCode>> oldCodes : entries) {
						addToCorrectQuadrant(oldCodes.getKey(), oldCodes.getValue());
					}
					codeCollection = null;
					return true;
				}
			}
		}
		
		private boolean add(Coordinate key, LinkedList<PostalCode> value) {
			codeCollection.put(key, value);
			return true;
		}
		
//		private boolean addToCorrectQuadrant(Coordinate key, LinkedList<PostalCode> value) {
//			//Lat > going North, Lat < going South
//			if(key.getLatitude() > this.nodeCoordinate.getLatitude()) {
//				if(key.getLongitude() > this.nodeCoordinate.getLongitude()) {
//					return ne.add(key);
//				} else {
//					return nw.add(key);
//				}
//			} else {
//				if(key.getCoordinate().getLongitude() > this.nodeCoordinate.getLongitude()) {
//					return se.add(key);
//				} else {
//					return sw.add(key);
//				}
//			}
//		}
		
		private boolean addToCorrectQuadrant(Coordinate key, LinkedList<PostalCode> value) {
			if (ne.containsCoordinate(key)) 
				return ne.add(key, value);
			else if (nw.containsCoordinate(key))
				return nw.add(key, value);
			else if (se.containsCoordinate(key))
				return se.add(key, value);
			else if (sw.containsCoordinate(key))
				return sw.add(key, value);
			 else {
				return false;
			}
		}
		
		private boolean addToCorrectQuadrant1(Coordinate key, LinkedList<PostalCode> value) {
			if (ne != null) {
				QuadNode closestNode = ne;
				if (GeodeticCalculator.distVincenty(key, nw.coordCenter) <= GeodeticCalculator
						.distVincenty(key, closestNode.coordCenter)) {
					closestNode = nw;
				}
				if (GeodeticCalculator.distVincenty(key, sw.coordCenter) <= GeodeticCalculator
						.distVincenty(key, closestNode.coordCenter)) {
					closestNode = sw;
				}
				if (GeodeticCalculator.distVincenty(key, se.coordCenter) <= GeodeticCalculator
						.distVincenty(key, closestNode.coordCenter)) {
					closestNode = se;
				}
				return closestNode.add(key, value);
			} else {
				return false;
			}
		}
		
		private boolean addToCorrectQuadrant(PostalCode code) {
			if (ne.containsCoordinate(code.getCoordinate())) 
				return ne.add(code);
			else if (nw.containsCoordinate(code.getCoordinate()))
				return nw.add(code);
			else if (se.containsCoordinate(code.getCoordinate()))
				return se.add(code);
			else if (sw.containsCoordinate(code.getCoordinate()))
				return sw.add(code);
			else {
				return false;
			}
		}
		
		private boolean addToCorrectQuadrant1(PostalCode code) {
			if (ne != null) {
				QuadNode closestNode = ne;
				if (GeodeticCalculator.distVincenty(code.getCoordinate(), nw.coordCenter) <= GeodeticCalculator
						.distVincenty(code.getCoordinate(), closestNode.coordCenter)) {
					closestNode = nw;
				}
				if (GeodeticCalculator.distVincenty(code.getCoordinate(), sw.coordCenter) <= GeodeticCalculator
						.distVincenty(code.getCoordinate(), closestNode.coordCenter)) {
					closestNode = sw;
				}
				if (GeodeticCalculator.distVincenty(code.getCoordinate(), se.coordCenter) <= GeodeticCalculator
						.distVincenty(code.getCoordinate(), closestNode.coordCenter)) {
					closestNode = se;
				}
				return closestNode.add(code);
			} else {
				return false;
			}
		}
		
		private boolean containsCoordinate(Coordinate candidateCoordinate) {
			if (candidateCoordinate.getLatitude() <= topLeftCoordinate.getLatitude() && candidateCoordinate.getLatitude() >= bottomRightCoordinate.getLatitude() && 
					candidateCoordinate.getLongitude() >= topLeftCoordinate.getLongitude() && candidateCoordinate.getLongitude() <= bottomRightCoordinate.getLongitude())
				return true;
			else
				return false;
		} // ends containsCoordinate()
		
//			//if coordinate is more to the ne
//		private void addToCorrectQuadrant(PostalCode code) {
//			if(code.getCoordinate().getLatitude() >= nodeCoordinate.getLatitude() &&
//					code.getCoordinate().getLongitude() >= nodeCoordinate.getLongitude()) {
//				ne.add(code);
//			} else if(code.getCoordinate().getLatitude() >= nodeCoordinate.getLatitude() &&
//					code.getCoordinate().getLongitude() <= nodeCoordinate.getLongitude()) {
//				//if code coordinate is more to nw
//				nw.add(code);
//			} else if(code.getCoordinate().getLatitude() <= nodeCoordinate.getLatitude() &&
//					code.getCoordinate().getLongitude() >= nodeCoordinate.getLongitude()) {
//				//if code coordinate is more to se
//				se.add(code);
//			} else if(code.getCoordinate().getLatitude() <= nodeCoordinate.getLatitude() &&
//					code.getCoordinate().getLongitude() <= nodeCoordinate.getLongitude()) {
//				//if code coordinate is more to sw
//				sw.add(code);
//			}
//		}
		
//		private boolean addToCorrectQuadrant(PostalCode code) {
//			//Lat > going North, Lat < going South
//			if(code.getCoordinate().getLatitude() > this.nodeCoordinate.getLatitude()) {
//				if(code.getCoordinate().getLongitude() > this.nodeCoordinate.getLongitude()) {
//					return ne.add(code);
//				} else {
//					return nw.add(code);
//				}
//			} else {
//				if(code.getCoordinate().getLongitude() > this.nodeCoordinate.getLongitude()) {
//					return se.add(code);
//				} else {
//					return sw.add(code);
//				}
//			}
//		}
		
//		private QuadNode getCorrectQuadrant(Coordinate coordinate) {
//			if(coordinate.getLatitude() >= nodeCoordinate.getLatitude() &&
//					coordinate.getLongitude() >= nodeCoordinate.getLongitude()) {
//				System.out.println("Current location: " + nodeCoordinate.toString() + ", going NE");
//				return ne;
//			} else if(coordinate.getLatitude() >= nodeCoordinate.getLatitude() &&
//					coordinate.getLongitude() <= nodeCoordinate.getLongitude()) {
//				//if code coordinate is more to nw
//				System.out.println("Current location: " + nodeCoordinate.toString() + ", going NW");
//				return nw;
//			} else if(coordinate.getLatitude() <= nodeCoordinate.getLatitude() &&
//					coordinate.getLongitude() >= nodeCoordinate.getLongitude()) {
//				//if code coordinate is more to se
//				System.out.println("Current location: " + nodeCoordinate.toString() + ", going SE");
//				return se;
//			} else if(coordinate.getLatitude() <= nodeCoordinate.getLatitude() &&
//					coordinate.getLongitude() <= nodeCoordinate.getLongitude()) {
//				//if code coordinate is more to sw
//				System.out.println("Current location: " + nodeCoordinate.toString() + ", going SW");
//				return sw;
//			}
//			return null;
//		}
		
//		private QuadNode getCorrectQuadrant(Coordinate coordinate) {
//			//Lat > going North, Lat < going South
//			if(coordinate.getLatitude() > this.nodeCoordinate.getLatitude()) {
//				if(coordinate.getLongitude() > this.nodeCoordinate.getLongitude()) {
//					return ne;
//				} else {
//					return nw;
//				}
//			} else {
//				if(coordinate.getLongitude() > this.nodeCoordinate.getLongitude()) {
//					return se;
//				} else {
//					return sw;
//				}
//			}
//		}
		
		private QuadNode getCorrectQuadrant(Coordinate coordinate) {
			if (ne.containsCoordinate(coordinate)) 
				return ne;
			else if (nw.containsCoordinate(coordinate))
				return nw;
			else if (se.containsCoordinate(coordinate))
				return se;
			else if (sw.containsCoordinate(coordinate))
				return sw;
			else
				return this;
		}
		
		private QuadNode getClosestQuadrant(Coordinate coordinate) {
			if (ne != null) {
				QuadNode closestNode = ne;
				if (GeodeticCalculator.distVincenty(coordinate, nw.coordCenter) <= GeodeticCalculator
						.distVincenty(coordinate, closestNode.coordCenter)) {
					closestNode = nw;
				}
				if (GeodeticCalculator.distVincenty(coordinate, sw.coordCenter) <= GeodeticCalculator
						.distVincenty(coordinate, closestNode.coordCenter)) {
					closestNode = sw;
				}
				if (GeodeticCalculator.distVincenty(coordinate, se.coordCenter) <= GeodeticCalculator
						.distVincenty(coordinate, closestNode.coordCenter)) {
					closestNode = se;
				}
				return closestNode;
			} else {
				return this;
			}
		}
		
//		private LinkedList<PostalCode> findClosest(Coordinate coordinate) {
//			double currentDistance = GeodeticCalculator.distVincenty(coordinate, this.nodeCoordinate);
//			QuadNode next  = getCorrectQuadrant(coordinate);
//			
//			if(next != null && next.nodeCoordinate != null && currentDistance > GeodeticCalculator.distVincenty(coordinate, next.nodeCoordinate)) {
//				System.out.println("Distance to next :" + GeodeticCalculator.distVincenty(coordinate, this.nodeCoordinate));
//				return next.findClosest(coordinate);
//			} else {
//				return sameCoordinatesCollection;
//			}
//		}
		
//		private LinkedList<PostalCode> findClosest(Coordinate coordinate) {
//			QuadNode next = getCorrectQuadrant(coordinate);
//			
//			if(next != null && next.codeCollection == null) {
//				return next.findClosest(coordinate);
//			} else {
//				Set<Entry<Coordinate, LinkedList<PostalCode>>> entries = codeCollection.entrySet();
//				LinkedList<PostalCode> closestValues = null;
//				for(Entry<Coordinate, LinkedList<PostalCode>> entry : entries) {
//					int index = 0;
//					if(index == 0)
//						closestValues = entry.getValue();
//					if(GeodeticCalculator.distVincenty(entry.getValue().getFirst().getCoordinate(), coordinate) < GeodeticCalculator.distVincenty(closestValues.getFirst().getCoordinate(), coordinate))
//						closestValues = entry.getValue();
//					++index;
//				}
//				return closestValues;
//			}
//		}
		
		private LinkedList<PostalCode> findClosest(Coordinate coordinate) {
			
			if(!containsCoordinate(coordinate)) {
				return null;
			}
			
			QuadNode next = getClosestQuadrant(coordinate);
			
			if(next != null && next.codeCollection == null) {
				return next.findClosest(coordinate);
			} else {
				Set<Entry<Coordinate, LinkedList<PostalCode>>> entries = next.codeCollection.entrySet();
				LinkedList<PostalCode> closestValues = null;
				for(Entry<Coordinate, LinkedList<PostalCode>> entry : entries) {
					int index = 0;
					System.out.println(entry.getKey());
					if(index == 0)
						closestValues = entry.getValue();
					if(GeodeticCalculator.distVincenty(entry.getValue().getFirst().getCoordinate(), coordinate) < GeodeticCalculator.distVincenty(closestValues.getFirst().getCoordinate(), coordinate))
						closestValues = entry.getValue();
					++index;
				}
				System.out.println(closestValues);
				return closestValues;
			}
		}
		
		private LinkedList<PostalCode> findClosest2(Coordinate coordinate) {
//			if(!containsCoordinate(coordinate)) {
//				return null;
//			}
			
			QuadNode next = getClosestQuadrant(coordinate);
			
			if(next != null && next.codeCollection == null) {
				return next.findClosest2(coordinate);
			} else {
				LinkedList<PostalCode> closestValues;
				if(next.codeCollection != null && next.codeCollection.size() != 0) {
					Set<Entry<Coordinate, LinkedList<PostalCode>>> entries = next.codeCollection
							.entrySet();
					closestValues = null;
					System.out.println("Distances in leaf: ");
					for (Entry<Coordinate, LinkedList<PostalCode>> entry : entries) {
						int index = 0;
						System.out.println(entry.getKey());
						if (index == 0)
							closestValues = entry.getValue();
						if (GeodeticCalculator.distVincenty(entry.getValue().getFirst().getCoordinate(), coordinate) < 
								GeodeticCalculator.distVincenty(closestValues.getFirst().getCoordinate(), coordinate))
							closestValues = entry.getValue();
						++index;
						System.out.println(GeodeticCalculator.distVincenty(entry.getValue()
								.getFirst().getCoordinate(), coordinate));
					}
					return closestValues;
				} else {
					return getOtherClosestQuadrant(coordinate, next).findClosest2(coordinate);
					//go down all 3 other branches and find closest match 
				}
			}
		}
		
		private LinkedList<PostalCode> findClosest3(Coordinate coordinate) {
//			if(!containsCoordinate(coordinate)) {
//				return null;
//			}
			
			QuadNode next = getClosestQuadrant(coordinate);
			System.out.println(next.coordCenter+ " Distance: " + GeodeticCalculator.distVincenty(next.coordCenter, coordinate) );
			//QuadNode next = getCorrectQuadrant(coordinate);
			
			if(next != null && next.codeCollection == null) {
				return next.findClosest3(coordinate);
			} else {
				if(next.codeCollection != null && next.codeCollection.size() != 0) {
					LinkedList<PostalCode> closestValues;
					Set<Entry<Coordinate, LinkedList<PostalCode>>> entries = next.codeCollection
							.entrySet();
					closestValues = null;
					System.out.println("Distances in leaf: ");
					int index = 0;
					for (Entry<Coordinate, LinkedList<PostalCode>> entry : entries) {
						if (index++ == 0)
							closestValues = entry.getValue();
						if (GeodeticCalculator.distVincenty(entry.getValue().getFirst().getCoordinate(), coordinate) < 
								GeodeticCalculator.distVincenty(closestValues.getFirst().getCoordinate(), coordinate)) {
							closestValues = entry.getValue();
						}
						System.out.println(entry.getValue().getFirst() + "Distance: " +GeodeticCalculator.distVincenty(entry.getValue()
								.getFirst().getCoordinate(), coordinate));
					}
					return closestValues;
				} else {
//					return getOtherClosestQuadrant(coordinate, next).findClosest3(coordinate);
					//go down all 3 other branches and find closest match 
					if(next.equals(ne)) {
						return determineClosestList(coordinate,nw.findClosest3(coordinate), sw.findClosest3(coordinate), se.findClosest3(coordinate));
					} else if(next.equals(nw)) {
						return determineClosestList(coordinate,ne.findClosest3(coordinate), sw.findClosest3(coordinate), se.findClosest3(coordinate));
					} else if(next.equals(se)) {
						return determineClosestList(coordinate,nw.findClosest3(coordinate), sw.findClosest3(coordinate), ne.findClosest3(coordinate));
					} else if(next.equals(sw)) {
						return determineClosestList(coordinate,nw.findClosest3(coordinate), ne.findClosest3(coordinate), se.findClosest3(coordinate));
					} else {
						return null;
					}			
				}
			}
		}
		


		private LinkedList<PostalCode> determineClosestList(Coordinate coordinate,LinkedList<PostalCode> one, LinkedList<PostalCode> two, LinkedList<PostalCode> three) {
			System.out.println("One: " + one + "\nTwo: " + two + "\nThree: " + three);
			if(one != null && two != null && three != null) {
				if(GeodeticCalculator.distVincenty(one.getFirst().getCoordinate(), coordinate) < 
						GeodeticCalculator.distVincenty(two.getFirst().getCoordinate(), coordinate) &&
						GeodeticCalculator.distVincenty(one.getFirst().getCoordinate(), coordinate) < 
						GeodeticCalculator.distVincenty(three.getFirst().getCoordinate(), coordinate)) {
					return one;
				} else if(GeodeticCalculator.distVincenty(two.getFirst().getCoordinate(), coordinate) < 
						GeodeticCalculator.distVincenty(one.getFirst().getCoordinate(), coordinate) &&
						GeodeticCalculator.distVincenty(two.getFirst().getCoordinate(), coordinate) < 
						GeodeticCalculator.distVincenty(three.getFirst().getCoordinate(), coordinate)){
					return two;
				} else if(GeodeticCalculator.distVincenty(three.getFirst().getCoordinate(), coordinate) < 
						GeodeticCalculator.distVincenty(one.getFirst().getCoordinate(), coordinate) &&
						GeodeticCalculator.distVincenty(three.getFirst().getCoordinate(), coordinate) < 
						GeodeticCalculator.distVincenty(two.getFirst().getCoordinate(), coordinate)){
					return three;
				} else {
					return null;
				}
			} else if(one != null && two == null && three == null) {
				return one;
			} else if(one == null && two != null && three == null) {
				return two;
			} else if(one == null && two == null && three != null) {
				return three;
			} else if(one == null && two != null && three != null) {
				//TODO one null, two three not
				if(GeodeticCalculator.distVincenty(two.getFirst().getCoordinate(), coordinate) < 
						GeodeticCalculator.distVincenty(three.getFirst().getCoordinate(), coordinate)) {
					return two;
				} else {
					return three;
				}
			} else if(one != null && two == null && three != null) {
				//TODO two null, one three not
				if(GeodeticCalculator.distVincenty(one.getFirst().getCoordinate(), coordinate) < 
						GeodeticCalculator.distVincenty(three.getFirst().getCoordinate(), coordinate)) {
					return one;
				} else {
					return three;
				}
			} else if(one != null && two != null && three == null) {
				//TODO three null, one two not
				if(GeodeticCalculator.distVincenty(two.getFirst().getCoordinate(), coordinate) < 
						GeodeticCalculator.distVincenty(one.getFirst().getCoordinate(), coordinate)) {
					return two;
				} else {
					return one;
				}
			} else {
				return null;
			}
		}
		
		private QuadNode getOtherClosestQuadrant(Coordinate coordinate, QuadNode node) {
				if(node.equals(ne)) {
					QuadNode closestNode = nw;
					if (GeodeticCalculator.distVincenty(coordinate, nw.coordCenter) < GeodeticCalculator
							.distVincenty(coordinate, closestNode.coordCenter)) {
						closestNode = se;
					}
					if (GeodeticCalculator.distVincenty(coordinate, sw.coordCenter) < GeodeticCalculator
							.distVincenty(coordinate, closestNode.coordCenter)) {
						closestNode = sw;
					}
					return closestNode;
				} else if (node.equals(nw)) {
					QuadNode closestNode = ne;
					if (GeodeticCalculator.distVincenty(coordinate, nw.coordCenter) < GeodeticCalculator
							.distVincenty(coordinate, closestNode.coordCenter)) {
						closestNode = se;
					}
					if (GeodeticCalculator.distVincenty(coordinate, sw.coordCenter) < GeodeticCalculator
							.distVincenty(coordinate, closestNode.coordCenter)) {
						closestNode = sw;
					}
					return closestNode;
				} else if (node.equals(se)) {
					QuadNode closestNode = nw;
					if (GeodeticCalculator.distVincenty(coordinate, nw.coordCenter) < GeodeticCalculator
							.distVincenty(coordinate, closestNode.coordCenter)) {
						closestNode = ne;
					}
					if (GeodeticCalculator.distVincenty(coordinate, sw.coordCenter) < GeodeticCalculator
							.distVincenty(coordinate, closestNode.coordCenter)) {
						closestNode = sw;
					}
					return closestNode;
				} else {
					QuadNode closestNode = nw;
					if (GeodeticCalculator.distVincenty(coordinate, nw.coordCenter) < GeodeticCalculator
							.distVincenty(coordinate, closestNode.coordCenter)) {
						closestNode = se;
					}
					if (GeodeticCalculator.distVincenty(coordinate, sw.coordCenter) < GeodeticCalculator
							.distVincenty(coordinate, closestNode.coordCenter)) {
						closestNode = ne;
					}
					return closestNode;
				}
		}
		
		@Override
		public String toString() {
			return codeCollection + " ";
		}
	} // end inner class Node
	// End inner class Node
	//***************************************************************************
	//***************************************************************************
} // end class BinarySearchTree