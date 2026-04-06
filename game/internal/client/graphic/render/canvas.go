package render

// Canvas prepares to render renderable objects. Objects will be rendered in
// FIFO method. So, renderable don't have his own abstraction due to performance and
// app simplification, therefore the number of such primitive objects is limited
type Canvas struct {
}
