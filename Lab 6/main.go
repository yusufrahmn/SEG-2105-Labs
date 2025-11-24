package main

import (
	"fmt"
	"io"
	"net/http"
	"sync"
)

// Structure to store results
type FetchResult struct {
	URL        string
	StatusCode int
	Size       int
	Error      error
}

var wg sync.WaitGroup

// Worker function
func worker(id int, jobs <-chan string, results chan<- FetchResult) {
	defer wg.Done()

	for url := range jobs {
		// TODO: fetch the URL

		response, error := http.Get(url)
		if error != nil {
			results <- FetchResult{
				URL:   url,
				Error: error,
			}
			continue
		}

		body, readError := io.ReadAll(response.Body)
		response.Body.Close()

		if readError != nil {
			results <- FetchResult{
				URL:        url,
				StatusCode: response.StatusCode,
				Error:      readError,
			}
			continue
		}

		results <- FetchResult{
			URL:        url,
			StatusCode: response.StatusCode,
			Size:       len(body),
		}
	}
}

func main() {
	urls := []string{
		"https://example.com",
		"https://golang.org",
		"https://uottawa.ca",
		"https://github.com",
		"https://httpbin.org/get",
		"https://yusufr.com",
		"https://seuo-uosu.com",
		"https://ludic.mataroa.blog/",
		"https://sidhion.com/blog",
		"https://paperless.blog/",
		"https://phils-web-site.net/",
		"https://mirawelner.com/",
		"https://deadsimpletech.com/blog",
		"https://entropic.mataroa.blog/",
		"https://yakirhavin.com/",
	}

	numWorkers := 5

	jobs := make(chan string, len(urls))
	results := make(chan FetchResult, len(urls))

	fmt.Println("Fetching URLs concurrently using worker pool...\n")

	// TODO: Start workers

	for workerId := 1; workerId <= numWorkers; workerId++ {
		wg.Add(1)
		go worker(workerId, jobs, results)
	}

	// TODO: Send jobs

	for _, url := range urls {
		jobs <- url
	}

	close(jobs)

	// TODO: Collect results

	for index := 0; index < len(urls); index++ {
		result := <-results
		if result.Error == nil {
			fmt.Printf("%s | Status: %d | Size: %d bytes \n", result.URL, result.StatusCode, result.Size)
		} else {
			fmt.Printf("%s | Error: %s \n", result.URL, result.Error)
		}
	}

	wg.Wait()

	close(results)

	fmt.Println("\nScraping complete!")
}