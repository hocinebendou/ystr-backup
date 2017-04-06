library(disclapmix)

setwd("/home/hocine/Rserve")
#danes <- read.table("../danes.txt.gz", header=T, quote="\"", sep="\t")
danes <- read.table("Bdr9.csv", header=T, quote="\"", sep=",")

mikl <- function() {
  clusters <- 1L:3L
  db <- as.matrix(danes[rep(1:nrow(danes), danes$n), 1:(ncol(danes)-1)])
  #db <- as.matrix(danes[1:nrow(danes), 1:(ncol(danes)-1)]) # If the duplicates are not removed
  db <- as.matrix(apply(db, 2, as.integer))
  res <- lapply(clusters, function(clusters) disclapmix(db, clusters = clusters))
  marginalBICs <- sapply(res, function(fit) fit$BIC_marginal)
  bestfit <- res[[which.min(marginalBICs)]]
  
  disclap_estimates <- predict(bestfit, newdata = as.matrix(danes[, 1:(ncol(danes) - 1)]))
  
  print(sum(danes$n == 1) / (sum(danes$n) + 1))
  
  print(1 - sum(disclap_estimates))
  
  plot(danes$n/sum(danes$n), disclap_estimates, 
       xlab = "Observed frequency",
       ylab = "Predicted frequency using the discrete Laplace method")
  abline(a = 0, b = 1)
}


# count duplicate lines
# d <- read.table("B.csv", header=T, quote="\"", sep=",")
# db <- d[1:nrow(d), 2:ncol(d)]
# db <- as.matrix(apply(db, 2, as.integer))
# df <- data.frame(db)
# ones <- rep(1, nrow(df))
# df <- aggregate( ones, by = as.list(df), FUN = sum)
# write.table(df, file = "foo.csv", quote = TRUE, sep = ",", row.names = T, col.names = NA, qmethod = "double")
# write.table(df, file = "BNdr.csv", quote = TRUE, sep = ",", row.names = F, col.names = T, qmethod = "double")

# remove columns from df
# f <- subset(df, select = -c(Pop, DYS385a, DYS385b))
