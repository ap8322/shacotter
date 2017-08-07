package api.utils

trait Iso[T, U] {
  def to(t: T): U
  def from(u: U):T
}
