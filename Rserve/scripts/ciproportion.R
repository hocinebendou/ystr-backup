# Compute confidence interval

ciproportion <- function(haplocount, totalcount, observed=TRUE) {
  matchperobserved <-  totalcount %/% haplocount
  if(observed == FALSE) {
    matchperobserved <- (totalcount + 1) %/% (haplocount + 1)
  }
  pbar <- 1 / matchperobserved
  se <- sqrt(pbar * (1 - pbar)/totalcount)
  me <- qnorm(.975) * se
  ci <- 1 / (pbar + c(-me, me))
  if (ci[[1]] < 0 || ci[[1]] > totalcount) {
    ci[[1]] <- totalcount
  }
  print(ci)
  return(ci)
}