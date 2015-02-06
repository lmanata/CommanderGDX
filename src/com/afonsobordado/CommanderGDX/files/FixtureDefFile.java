package com.afonsobordado.CommanderGDX.files;

public class FixtureDefFile {
		
		private float density;
		private float friction;
		private float restitution;
		private short cb;
		private short mb;
		
		public FixtureDefFile(){}
		public FixtureDefFile(float density, float friction, float restitution, short cb, short mb){
			this.density = density;
			this.friction = friction;
			this.restitution = restitution;
			this.cb = cb;
			this.mb = mb;
		}
		
		public float getDensity() {
			return density;
		}
		public float getFriction() {
			return friction;
		}
		public float getRestitution() {
			return restitution;
		}
		public short getCb() {
			return cb;
		}
		public short getMb() {
			return mb;
		}
		


}
