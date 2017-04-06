library(disclapmix)
sim.sample <- function() {
  n <- 500
  number.of.loci <- 7
  types <- sample(x = 1:nrow(pop), size = n, replace = TRUE, prob = pop$N)
  types.table <- table(types)
  alpha <- sum(types.table == 1)
  print(alpha)
  dataset <- pop[as.integer(names(types.table)), ]
  dataset$Ndb <- types.table
  db <- pop[types, 1:number.of.loci]
  db <- as.matrix(apply(db, 2, as.integer))
  
  fit <- disclapmix(db, clusters = 1L)
  pred.popfreqs <- predict(fit, newdata = as.matrix(apply(dataset[, 1:number.of.loci], 2, as.integer)))
  return(list(pred.popfreqs, dataset$PopFreq))
}