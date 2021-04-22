
object OS {
  lazy val name: String = sys.props("os.name").toLowerCase match {
    // more details https://github.com/trustin/os-maven-plugin#property-osdetectedname
    case mac if mac.contains("mac") || mac.contains("darwin") => "osx"
    case linux if linux.startsWith("linux") => "linux"
    case n =>  sys.error(s"Unknown OS name: '$n'")
  }

  lazy val arch: String = sys.props("os.arch").toLowerCase.replaceAll("[^A-Za-z0-9]", "") match {
    // more details https://github.com/trustin/os-maven-plugin#property-osdetectedarch
    case x64 if List("x8664", "amd64", "ia32e", "em64t", "x64").contains(x64)  => "x86_64"
    case x32 if List("x8632", "x86", "i386", "i486", "i586", "i686", "ia32", "x32").contains(x32) => "x86_32"
    case n =>  sys.error(s"Unknown OS arch: '$n'")
  }

  def mac = s"osx-${arch}"
  def linux = s"linux-${arch}"
  def classifier = s"${name}-${arch}"
}
