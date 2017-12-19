#!/usr/bin/env perl
BEGIN {
    use CPAN;
    unless ( defined $ENV{SKIP_CPAN} ) {

        # List required distributions here:
        for my $i (
            qw/
            Modern::Perl
            Carp
            Data::Printer
            LWP
            LWP::Protocol::https
            URI::Query
            JSON
            JSON::Color
            URL::Encode
            /
          )
        {
            CPAN::Shell->notest( 'install', $i );
        }
    }
}
use Modern::Perl;
use Carp;
use Data::Printer;
use LWP;
use URI::Query;
use JSON;
use JSON::Color;
use URL::Encode qw(url_encode);

use constant AUTHORIZATION_VAL_PREFIX => "KakaoAK";

sub report_lwp_error {
    my ( $res, $url ) = @_;
    print STDERR np( $res->status_line ) . "\t" . np($url) . "\n";
    print STDERR 'RESPONSE: ', np( $res->content ), "\n";
    confess "FAIL";
}

sub get_access_token {
    my ( $base_url, $app_key, $redirect_uri ) = @_;
    my $params = {
        client_id     => $app_key,
        redirect_uri  => $redirect_uri,
        response_type => 'code',
    };
    my $qs  = URI::Query->new(%$params)->stringify;
    my $url = "${base_url}/oauth/authorize?${qs}";
    my $ua  = LWP::UserAgent->new;
    my $req = HTTP::Request->new( GET => $url );
    my $res = $ua->request($req);
    if ( $res->is_success ) {
        return $res->content;
    }
    else {
        report_lwp_error( $res, $url );
    }
}

sub search_books {
    my ( $base_url, $app_key, $query ) = @_;
    my $params   = { target => 'title', };
    my $qs       = URI::Query->new(%$params)->stringify;
    my $qs_query = url_encode($query);
    my $url      = "${base_url}/v2/search/book?${qs}&query=${qs_query}";
    $url = 'https://dapi.kakao.com//v2/search/book?size=10&query=test&sort=sales&page=1&target=all';
    p $url;
    #
    my $ua = LWP::UserAgent->new;
    my $req = HTTP::Request->new( GET => $url );
    $req->header(
        'Authorization' => AUTHORIZATION_VAL_PREFIX . ' ' . $app_key );
    p $req->header('Authorization');
    my $res = $ua->request($req);
    if ( $res->is_success ) {
        return $res->content;
    }
    else {
        report_lwp_error( $res, $url );
    }
}

#
{
    my $app_key = '9b7471bbb7d0d1405fa11afbca9f4856';    # "REST API Key".

# NO NEED.
#my $resp_at = get_access_token('https://kauth.kakao.com', $app_key, 'http://foobar.com/oauth');
#p $resp_at;

    my $keyword = "한글";
    my $resp_search =
      search_books( 'https://dapi.kakao.com', $app_key, $keyword );
    say JSON::Color::encode_json( decode_json($resp_search), { pretty => 1 } );
}
