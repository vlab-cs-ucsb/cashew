import sys, time, socket, argparse, errno
from socket import error as socket_error

__VERSION__ = '0.2'

__LOGO__ = '''                  __ _ _           
 _ __  _ __ ___  / _(_) | ___ _ __ 
| '_ \\| '__/ _ \\| |_| | |/ _ \\ '__|
| |_) | | | (_) |  _| | |  __/ |   
| .__/|_|  \\___/|_| |_|_|\\___|_|  vV
|_|                                
'''.replace('V', __VERSION__)


class Candidate(object):
	def __init__(self, his, los):
		self.his = his
		self.los = los

def candidates_from_list(tokens):
	candidate_list = []
	while(len(tokens) > 0):
		assert len(tokens) >= 2, "Expected <hiLen> <loLen>, but got %s" % str(tokens)
		hilen = int(tokens[0])
		lolen = int(tokens[1])
		tokens = tokens[2:]
		n = hilen + lolen
		assert len(tokens) >= n, "Expected at least %d + %d tokens, but got %s" % (hilen, lolen, str(tokens))
		his = tokens[:hilen]
		los = tokens[hilen:n]
		tokens = tokens[n:]
		candidate_list.append(Candidate(his, los))
	return candidate_list

def candidates_from_file(pathname):
	with open(pathname) as f:
		return candidates_from_list(f.read().split())

def take_sample(appid, candidate, host, port, verbose=False, BUFFER_SIZE=1024):
	if verbose:
		print("Sampling %s with hi: %s, lo: %s" % (appid, candidate.his, candidate.los))
	try:
		s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
		s.connect((host, port))
		# Send the application ID.
		s.send(appid + '\n')
		# Now the lengths of the high and low.
		s.send(str(len(candidate.his)) + '\n')
		s.send(str(len(candidate.los)) + '\n')
		# Then all components of the high.
		for hi in candidate.his:
			s.send(hi + '\n')
		# And all components of the low.
		for lo in candidate.los:
			s.send(lo + '\n')
		# Start the timer right after sending the last component of the low.
		# PEND: Should we be starting it *before* sending the last low instead?
		before = time.time()
		response = s.recv(BUFFER_SIZE)
		elapsed = time.time() - before
		s.close()
		# The observable (side channel) is time elapsed -- a float.
		# The response is the string returned by the server.
		observable = elapsed
		response = response.strip()
		return response, observable
	except socket_error as serr:
		if serr.errno == errno.ECONNREFUSED:
			sys.stderr.write("Connection refused by %s on port %d -- is the server up?\n" % (host, port))
			sys.exit(2)
		else: # Some other socket error -- re-raise it.
			raise serr

def print_csv(results, func=(lambda t: t), delimiter=','):
	# This will apply func to each tuple before printing.
	for i in range(len(results)):
		print(delimiter.join([str(func(tup)) for tup in results[i]]))

def main(appid, nsamples, candidates, host, port, verbose=False):
	results = [list() for i in range(nsamples)]
	for i in range(nsamples):
		for candidate in candidates:
			returned, observed = take_sample(appid, candidate, host, port, verbose)
			results[i].append((returned, observed))
	if verbose:
		print("")
	return results


if __name__ == '__main__':

	parser = argparse.ArgumentParser(description='Sample a remote application and return a matrix of measurements.')

	parser.add_argument('candidates', metavar='CANDIDATES', type=str, nargs='*',
		help='list of HILEN LOLEN H1 H2 ... L1 L2 ...')
	parser.add_argument('--app', '-a', metavar='APPID', type=str, default='add',
		help='identifies the target app on the server side')
	parser.add_argument('--samples', '-s', metavar='NUMBER', type=int, default=10,
		help='number of samples per candidate')
	parser.add_argument('--file', '-f', metavar='PATHNAME', type=str,
		help='read candidates from a file instead of command line')
	parser.add_argument('--host', metavar='ADDRESS', type=str, default='127.0.0.1',
		help='hostname or IP address of the server')
	parser.add_argument('--port', metavar='PORT', type=int, default=3456,
		help='TCP port that the server listens on')
	parser.add_argument('--verbose', '-v', action='store_true',
		help='print debug output')

	args = parser.parse_args()

	if not (args.candidates or args.file):
		print("\nPlease provide some candidates, or use the --file option.\n")
		parser.print_help()
		sys.exit(1)

	if(args.file):
		candidate_objects = candidates_from_file(args.file)
	else:
		candidate_objects = candidates_from_list(args.candidates)

	if args.verbose:
		print(__LOGO__)
		print("Host: %s" % args.host)
		print("Port: %d" % args.port)
		print("Application: %s" % args.app)
		print("Number of candidates: %d" % len(candidate_objects))
		print("Samples per candidate: %d\n" % args.samples)

	matrix = main(args.app, args.samples, candidate_objects, args.host, args.port, args.verbose)

	# This is the only output in non-verbose mode.
	get_response = lambda tup: tup[0]
	get_observable = lambda tup: tup[1]
	print_csv(matrix, get_response)
	print_csv(matrix, get_observable)






