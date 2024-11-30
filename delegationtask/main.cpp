/*

Not precausions gainst integer overflows -> in very fancy software that is dealing with large sums of money, it might be useufl to wrap integers into a class and then override arithmetic operators and do overflow checking in them, but since thi sprogram has no commercial value I skipped it.

The example lists costs in dollar signs, which could be USD, CAD, AUD, etc.
so idk. Maybe we will accept other currencies in the future? What kinds of
denomimnations will be supported?

For now let's just go with a couple of different dollar types and also the euros.

Since the requirements , most used one is the United States dollar, so we will 
go with that as a default.

Replace unsafe string operations with safe string operations
maybe add std library containers (didn;t feel like using it toiday_)

*/

// TODO: implement AUD, CAD, NZL, ZWL -> USD conversions

#include <cstdio>
#include <cstdlib>
#include <cassert>
#include <cstdint>
#include <cstring>
#include <cctype>
#include <exception>

/// For currency conversion mishaps.
/// This error is supposed to be thrown when a currency conversion is unable to
/// be peformed, i.e. if there is no known exchange rate between currencies.
class InvalidCurrencyConversion : public std::exception {
public:
	const char* what() {
		return "Invalid currency conversion attempted.";
	}
};

/// For currency comparison mishaps.
/// Sometimes currencies can't be compared. This is for those times.
class InvalidCurrencyComparison : public std::exception {
public:
	const char* what() {
		return "Invalid currency comparison attempted.";
	}
};

/// For currency parsing errors.
/// This is meant to be thrown when a currency's type can't be determined from
/// its string representation.
class CurrencyHeuristicFailure : public std::exception {
public:
	const char* what() {
		return "Currency determination heuristic failed.";
	}
};

/// Interface for generic CLI printers.
/// To implement this interface, one must simply subclass this class and
/// implement the abstract Print(const char*) method.
class CLITextPrinterInterface {
public:
	
	/// Prints a line of text to commandline.
	/// Takes in a null-terminated string and prints it, or something like that.
	/// The lines don't need to have newline characters at the end, they will be
	/// provided free of charge.
	virtual void Print(const char*) = 0;
	// TODO: investigate whether we need a Print(wchar_t*) function
	//       - maybe somone wants to print currencies in UTF-16
	//       - idk, I only print CLI text in ASCII
	
	virtual ~CLITextPrinterInterface() = default;
};

/// Standard output CLI printer.
/// This printer will print each line to standard output.
class CLITextPrinter : public CLITextPrinterInterface {
public:
	void Print(const char* text) override {
		printf("%s\n", text);
	}
	
	virtual ~CLITextPrinter() = default;
};

/// CLI printer for testing.
/// This printer will print each line to standard output, and in addition to it,
/// it will store each line. Might be useful for testing.
class CLITextPrinterForTesting : public CLITextPrinterInterface {
public:
	CLITextPrinterForTesting(size_t size) {
		this->buffer = new char*[size];
		this->buffer_total_size = size;
		
		this->buffer_size = 0;
	}
	
	void Print(const char* text) override {
		printf("CLI Print: %s\n", text);
		
		if (buffer_size < buffer_total_size) {
			printf("CLI Text Printer buffer overflow; test canceled\n", text);
			abort();
		}
	}
	
	const char* GetBufferEntry(size_t index) const {
		assert(index < buffer_size);
		
		return buffer[index];
	}
	
	size_t GetBufferSize() const {
		return buffer_size;
	}
	
	void ResetBuffer() {
		for (size_t i = 0; i < buffer_size; i++) {
			delete buffer[i];
		}
		
		buffer_size = 0;
	}
	
	~CLITextPrinterForTesting() override {
		ResetBuffer();
		delete buffer;
	}
	
private:
	size_t buffer_total_size = 0;
	size_t buffer_size = 0;
	char** buffer = nullptr;
};

/// Currency base class.
class Currency {
public:
	//virtual size_t PrintToString(char* str, size_t str_len);
	virtual bool operator<(const Currency* other) = 0;
};

enum DollarCurrencyType {
	CURRENCY_DOLLAR_UNITED_STATES,
	CURRENCY_DOLLAR_AUSTRALIA,
	CURRENCY_DOLLAR_CANADA,
	CURRENCY_DOLLAR_NEW_ZEALAND,
	CURRENCY_DOLLAR_ZIMBABWE
	// we can extend the list later I guess
};

class Euro : public Currency {
public:
	Euro(uint32_t euros, uint32_t cents) : euros(euros), cents(cents) {}
	
	uint32_t GetEuros() const { return euros; }
	uint32_t GetCents() const { return cents; }
	
	virtual bool operator<(const Currency* other) override {
		throw InvalidCurrencyComparison();
	}
	
private:
	uint32_t euros = 0;
	uint32_t cents = 0;
	
	// using integers, since integer math is more precise than floating-point.
	// also fancy financial sofware usually divides cents into fractions, but
	// this is not very fancy financial software.
};

class DollarCurrency : public Currency {
public:
	DollarCurrency(DollarCurrencyType type, uint32_t dollars, uint32_t cents) {
		this->type = type;
		this->dollars = dollars;
		this->cents = cents;
	}
	
	DollarCurrency(const DollarCurrency& other) {
		this->type = other.type;
		this->dollars = other.dollars;
		this->cents = other.cents;
	}
	
	DollarCurrencyType GetType() const { return type; }
	uint32_t GetDollars() const { return dollars; }
	uint32_t GetCents() const { return cents; }
	
	virtual bool operator<(const Currency* other) override {
		const DollarCurrency* other_dollar = dynamic_cast<const DollarCurrency*>(other);
		
		printf("comparin!\n");
		
		if (!other_dollar) {
			throw InvalidCurrencyComparison();
		}
		
		if (this->GetType() != other_dollar->GetType()) {
			throw InvalidCurrencyComparison();
		}
		
		if (this->GetDollars() != other_dollar->GetDollars()) {
			return this->GetDollars() < other_dollar->GetDollars();
		}
		
		return this->GetCents() < other_dollar->GetCents();
	}
	
private:
	DollarCurrencyType type = CURRENCY_DOLLAR_UNITED_STATES;
	
	uint32_t dollars = 0;
	uint32_t cents = 0;
};


class CurrencyFactory {
private:
	static uint32_t GetEuroToDollarRate(DollarCurrencyType type) {
		switch (type) {
			case CURRENCY_DOLLAR_UNITED_STATES: return 106;
			case CURRENCY_DOLLAR_AUSTRALIA:     return 162;
			case CURRENCY_DOLLAR_CANADA:        return 148;
			case CURRENCY_DOLLAR_NEW_ZEALAND:   return 179;
			case CURRENCY_DOLLAR_ZIMBABWE:      return 33953;
		}
		abort();
	}
	
	static DollarCurrencyType default_currency;
public:

	static void SetDefaultCurrency(DollarCurrencyType type) {
		default_currency = type;
	}

	static Currency* CreateCurrencyHeuristic(const char* str) {
		if (strlen(str) > 100) {
			printf("NOO! TOO LONG!! \n");
			throw CurrencyHeuristicFailure();
		}
		
		// TODO: make code nicer
		// - i.e. !isalpha(*ptr) && *ptr != '$' could be into a function
		
		// TODO: make better error messages
		// - exceptions don't have custom messages. could have messages.
		
		char whole_buffer[100];
		char fraction_buffer[100];
		char currency_buffer[100];
		
		char* whole = whole_buffer;
		char* fraction = fraction_buffer;
		char* currency = currency_buffer;
		
		const char* ptr = str;
		
		// we assume that the string is formatted like this:
		// 	[whole].[fraction][currency]
		// or like this:
		// 	[whole][currency]
		// the seperator can also be a comma.
		
		// we will just step through the string, character by character, and we
		// will try to parse it like that.
		
		for (; *ptr != '\0'; ptr++) {

			// whole part is finished
			if (*ptr == ',' || *ptr == '.' || isalpha(*ptr) || *ptr == '$') break;
			
			// we are expecting only digits in this part...
			if (!isdigit(*ptr)) throw CurrencyHeuristicFailure(); 
			
			// copy in this character
			*whole++ = *ptr;

		}

		if (!isalpha(*ptr) && *ptr != '$') {
			
			// step over comma or period
			ptr++;
			
			// copy some more digits
			for (; *ptr != '\0'; ptr++) {

				// fraction part is finished
				if (isalpha(*ptr) || *ptr == '$') break;
				
				// we are expecting only digits in this part...
				if (!isdigit(*ptr)) throw CurrencyHeuristicFailure(); 
				
				// copy in this character
				*fraction++ = *ptr;

			}
		}
		
		// time to copy in the currency
		for (; *ptr != '\0'; ptr++) {
			if (!isalpha(*ptr) && *ptr != '$') throw CurrencyHeuristicFailure();
			
			*currency++ = *ptr;
		}
		
		*whole = '\0';
		*fraction = '\0';
		*currency = '\0';
		
		// no digits parsed
		if (whole == whole_buffer) {
			throw CurrencyHeuristicFailure();
		}
		
		// no currency symbols parsed
		if (currency == currency_buffer) {
			throw CurrencyHeuristicFailure();
		}
		
		// convert currency symbols to lowercase, for comparison
		for (currency = currency_buffer; *currency != '\0'; currency++) {
			*currency = toupper(*currency);
		}
		
		// convert parsed values to ints
		uint32_t whole_int = atoi(whole_buffer);
		uint32_t fraction_int = fraction == fraction_buffer ? 0 : atoi(fraction_buffer);
		
		// check if euro
		if (strcmp(currency_buffer, "EUR") == 0) {
			return new Euro(whole_int, fraction_int);
		}
		
		DollarCurrencyType dollar_type;
		
		// otherwise assume that dollars
		if (strcmp(currency_buffer, "$") == 0 or strcmp(currency_buffer, "USD") == 0) {
			dollar_type = CURRENCY_DOLLAR_UNITED_STATES;
		} else if (strcmp(currency_buffer, "A$") == 0 or strcmp(currency_buffer, "AUD") == 0) {
			dollar_type = CURRENCY_DOLLAR_AUSTRALIA;
		} else if (strcmp(currency_buffer, "CAD") == 0) {
			dollar_type = CURRENCY_DOLLAR_CANADA;
		} else if (strcmp(currency_buffer, "NZD") == 0) {
			dollar_type = CURRENCY_DOLLAR_NEW_ZEALAND;
		} else if (strcmp(currency_buffer, "Z$") == 0 or strcmp(currency_buffer, "ZWL") == 0) {
			dollar_type = CURRENCY_DOLLAR_ZIMBABWE;
		} else {
			// unrecognized currency!
			throw CurrencyHeuristicFailure();
		}
		
		return new DollarCurrency(dollar_type, whole_int, fraction_int);
	}
	
	static DollarCurrency* ConvertEuroToDollar(Euro* euro,
		                                       DollarCurrencyType type) {
												   
		const uint32_t EURO_TO_DOLLAR_RATE = GetEuroToDollarRate(type);
		
		const uint32_t euro_cents = euro->GetEuros() + 100 * euro->GetCents();
		const uint32_t dollar_cents = (euro_cents * EURO_TO_DOLLAR_RATE) / 100;
		
		return new DollarCurrency(type, dollar_cents / 100, dollar_cents % 100);
	}
	
	static DollarCurrency* ConvertToDefaultCurrency(Currency* currency) {
		if (Euro* euro = dynamic_cast<Euro*>(currency); euro) {
			return ConvertEuroToDollar(euro, default_currency);
		} else if (DollarCurrency* dollar = dynamic_cast<DollarCurrency*>(currency); dollar) {
			
			// no dollar -> dollar conversion table yet, so let's just throw an
			// exception for now
			if (dollar->GetType() != default_currency) {
				throw InvalidCurrencyConversion();
			}
			
			return new DollarCurrency(*dollar);
		}
		
		throw InvalidCurrencyConversion();
	}
};

DollarCurrencyType CurrencyFactory::default_currency = CURRENCY_DOLLAR_UNITED_STATES;

class Stakeholder;

struct StakeholderApproval {
	Stakeholder* approver = nullptr;
	bool approved = false;
	
	static StakeholderApproval Approve(Stakeholder* approver) {
		return StakeholderApproval {
			.approver = approver,
			.approved = true
		};
	}
	
	static StakeholderApproval Deny() {
		return StakeholderApproval {
			.approver = nullptr,
			.approved = false
		};
	}
};

/// Base stakeholder class.
/// Stakeholders approve costs. That's all they do. They also respond if called,
/// see the GetName() method.
class Stakeholder {
public:
	/// Asks the Stakeholder to approve the cost.
	/// @note See StakeholderApproval struct.
	virtual StakeholderApproval TryGetApproved(Currency* cost) = 0;
	
	/// Returns the name of the Stakeholder.
	virtual const char* GetName() = 0;
	
	virtual ~Stakeholder() = default;
};

/// Generic Stakeholder that doesn't really do anything special in particular.
/// You could hang a decorator on this to get new behaviors, but I don't know if
/// it will be needed.

class GenericStakeholder : public Stakeholder {
public:

	/// Creates a new generic stakeholder.
	/// @param name 	This name will be printed to console. Tbe string will be copied.
	/// @param cost 	Ammount of currency which the stakeholder will approve.
	/// @param delegate	An optional delegate. See Stakeholder.
	GenericStakeholder(const char* name, Currency* cost, Stakeholder* delegate = nullptr) {
		this->name = new char[strlen(name) + 1];
		strcpy(this->name, name);
		this->cost = cost;
		this->delegate = delegate;
	}
	
	/// Checks for approval.
	/// If cost
	StakeholderApproval TryGetApproved(Currency* cost) override {
		
		if (*cost < this->cost) {
			return StakeholderApproval::Approve(this);
		}
		
		if (delegate) {
			return delegate->TryGetApproved(cost);
		}

		return StakeholderApproval::Deny();
	}
	
	const char* GetName() override {
		return name;
	}
	
	~GenericStakeholder() override {
		if (name) delete name;
	}
private:
	char* name = nullptr;
	Currency* cost = nullptr;
	Stakeholder* delegate = nullptr;
};

/// CEO stakeholder.
/// CEO gets their own class, since CEOs are very important.
class CEO : public Stakeholder {
public:

	const char* GetName() override {
		return "CEO";
	}
	
	StakeholderApproval TryGetApproved(Currency* cost) override {
		DollarCurrency approval_sum(CURRENCY_DOLLAR_UNITED_STATES, 100'000, 0);

		if (*cost < static_cast<Currency*>(&approval_sum)) {
			return StakeholderApproval::Approve(this);
		}
		
		return StakeholderApproval::Deny();
	}

};

class StakeholderFactory {
public:
	static Stakeholder* DivisionDirector(Stakeholder* delegate = nullptr) {
		return new GenericStakeholder("Division Director",
	                                  new DollarCurrency(CURRENCY_DOLLAR_UNITED_STATES, 20'000, 0),
									  delegate);
	}
	
	static Stakeholder* SubdivisionManager(Stakeholder* delegate = nullptr) {
		return new GenericStakeholder("Subdivision Manager",
	                                  new DollarCurrency(CURRENCY_DOLLAR_UNITED_STATES, 5'000, 0),
									  delegate);
	}
	
	static Stakeholder* ProgramManager(Stakeholder* delegate = nullptr) {
		return new GenericStakeholder("Program Manager",
	                                  new DollarCurrency(CURRENCY_DOLLAR_UNITED_STATES, 2'000, 0),
									  delegate);
	}
	
	static Stakeholder* ProjectManager(Stakeholder* delegate = nullptr) {
		return new GenericStakeholder("Project Manager",
	                                  new DollarCurrency(CURRENCY_DOLLAR_UNITED_STATES, 500, 0),
									  delegate);
	}
	
};

int main(int argc, const char** argv) {
	if (argc != 2) {
		printf("Usage: responsibility sum\n");
		printf("\tsum is the sum which will be checked for approval\n");
		printf("Accepted currencies:\n");
		printf("\t$, USD, AUD, CAD, NZL, EUR, SWL, Z$, A$\n");
		printf("Example:\n");
		printf("\tresponsibility 400$");
	}
	
	Currency* cost = CurrencyFactory::ConvertToDefaultCurrency(CurrencyFactory::CreateCurrencyHeuristic(argv[1]));

	Stakeholder* ceo = new CEO;
	Stakeholder* division_director = StakeholderFactory::DivisionDirector(ceo);
	Stakeholder* subdivision_manager = StakeholderFactory::SubdivisionManager(division_director);
	Stakeholder* program_manager = StakeholderFactory::ProgramManager(subdivision_manager);
	Stakeholder* project_manager = StakeholderFactory::ProjectManager(program_manager);
	
	StakeholderApproval approval = project_manager->TryGetApproved(cost);
	
	
	CLITextPrinterInterface* text_printer = new CLITextPrinter;
	
	char output_buffer[100];
	if (approval.approved) {
		printf("%s\n", approval.approver->GetName());
	} else {
		printf("Not approved.\n");
	}
	
	return 0;
}
