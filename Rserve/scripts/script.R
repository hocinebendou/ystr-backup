library(fwsim)

pop.sim <- function() {
  set.seed(1)
  generations <- 100L
  population.size <- 1e+05L
  number.of.loci <- 7
  mutation.rates <- seq(0.001, 0.01, length.out = number.of.loci)
  H0 <- matrix(c(0L, 0L, 0L, 0L, 0L, 0L, 0L), 1L, 7L, byrow = TRUE)
  
  sim <- fwsim(G=generations, H0=H0, N0=population.size, mutmodel = mutation.rates)
  pop <- sim$population
  
  y <- c(14, 12, 28, 22, 10, 11, 13)
  for (i in 1:number.of.loci) {
    pop[, i] <- pop[, i] + y[i]
  }
  
  return(pop)
}
